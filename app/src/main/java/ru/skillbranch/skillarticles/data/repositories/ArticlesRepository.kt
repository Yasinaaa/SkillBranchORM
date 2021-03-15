package ru.skillbranch.skillarticles.data.repositories

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.local.dao.*
import ru.skillbranch.skillarticles.data.local.entities.ArticleItem
import ru.skillbranch.skillarticles.data.local.entities.ArticleTagXRef
import ru.skillbranch.skillarticles.data.local.entities.CategoryData
import ru.skillbranch.skillarticles.data.local.entities.Tag
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.remote.err.NoNetworkError
import ru.skillbranch.skillarticles.data.remote.res.ArticleRes
import ru.skillbranch.skillarticles.extensions.data.toArticle
import ru.skillbranch.skillarticles.extensions.data.toArticleContent
import ru.skillbranch.skillarticles.extensions.data.toArticleCounts
import ru.skillbranch.skillarticles.extensions.data.toCategory
import javax.inject.Inject

interface IArticlesRepository: IRepository {

    fun findTags(): LiveData<List<String>>
    fun findCategoriesData(): LiveData<List<CategoryData>>
    fun rawQueryArticles(filter: ArticleFilter): DataSource.Factory<Int, ArticleItem>
    suspend fun incrementTagUseCount(tag: String)
    suspend fun loadArticlesFromNetwork(start: String? = null, size: Int = 10): Int
    suspend fun toggleBookmark(articleId: String): Boolean
    suspend fun findLastArticleId(): String?
    suspend fun fetchArticleContent(articleId: String)
    suspend fun removeArticleContent(articleId: String)
}

class ArticlesRepository @Inject constructor(
    private val network: RestService,
    private val prefs: PrefManager,
    private var articlesDao: ArticlesDao,
    private var articlesContentDao: ArticleContentsDao,
    private var articleCountsDao: ArticleCountsDao,
    private var categoriesDao: CategoriesDao,
    private var tagsDao: TagsDao,
    private var articlePersonalDao: ArticlePersonalInfosDao
) : IArticlesRepository {

    override fun findTags(): LiveData<List<String>> {
        return tagsDao.findTags()
    }

    override fun findCategoriesData(): LiveData<List<CategoryData>> {
        return categoriesDao.findAllCategoriesData()
    }

    override fun rawQueryArticles(filter: ArticleFilter): DataSource.Factory<Int, ArticleItem> {
        return articlesDao.findArticlesByRaw(SimpleSQLiteQuery(filter.toQuery()))
    }

    override suspend fun incrementTagUseCount(tag: String) {
        tagsDao.incrementTagUseCount(tag)
    }

    override suspend fun loadArticlesFromNetwork(start: String?, size: Int): Int {
        val items = network.articles(start, size)
        if (items.isNotEmpty()) insertArticlesToDb(items)
        return items.size
    }

    override suspend fun toggleBookmark(articleId: String): Boolean {
        return articlePersonalDao.toggleBookmarkOrInsert(articleId)
    }

    override suspend fun findLastArticleId(): String? = articlesDao.findLastArticleId()

    private suspend fun insertArticlesToDb(articles: List<ArticleRes>) {
        articlesDao.upsert(articles.map { it.data.toArticle() })
        articleCountsDao.upsert(articles.map { it.counts.toArticleCounts() })

        val refs = articles.map { it.data }
            .fold(mutableListOf<Pair<String, String>>()) { acc, res ->
                acc.also { list -> list.addAll(res.tags.map { res.id to it }) }
            }

        val tags = refs.map { it.second }
            .distinct()
            .map { Tag(it) }

        val categories = articles.map { it.data.category.toCategory() }

        categoriesDao.insert(categories)
        tagsDao.insert(tags)
        tagsDao.insertRefs(refs.map { ArticleTagXRef(it.first, it.second) })
    }

    override suspend fun fetchArticleContent(articleId: String) {
        val content = network.loadArticleContent(articleId)
        articlesContentDao.insert(content.toArticleContent())
    }

    override suspend fun removeArticleContent(articleId: String) {
        articlesContentDao.delete(articleId)
    }

    suspend fun addBookmark(articleId: String) {
        if (prefs.accessToken.isEmpty()) return
        try {
            network.addBookmark(articleId, prefs.accessToken)
        }catch (e:Throwable){
            if (e is NoNetworkError) return
            throw e
        }
    }

    suspend fun removeBookmark(articleId: String) {
        if (prefs.accessToken.isEmpty()) return
        try {
            network.removeBookmark(articleId, prefs.accessToken)
        }catch (e:Throwable){
            if (e is NoNetworkError) return
            throw e
        }
    }
}

class ArticleFilter(
    val search: String? = null,
    val isBookmark: Boolean = false,
    val categories: List<String> = listOf(),
    val isHashtag: Boolean = false
) {
    fun toQuery(): String {
        val qb = QueryBuilder()

        qb.table("ArticleItem")

        if (search != null && !isHashtag) qb.appendWhere("title LIKE '%$search%'")
        if (search != null && isHashtag) {
            qb.innerJoin("article_tag_x_ref AS refs", "refs.a_id = id")
            qb.appendWhere("refs.t_id = '$search'")
        }
        if (isBookmark) qb.appendWhere("is_bookmark = 1")
        if (categories.isNotEmpty()) qb.appendWhere("category_id IN (${categories.joinToString(",")})")

        qb.orderBy("date")
        return qb.build()
    }
}

class QueryBuilder() {
    private var table: String? = null
    private var selectColumns: String = "*"
    private var joinTables: String? = null
    private var whereCondition: String? = null
    private var order: String? = null


    fun table(table: String): QueryBuilder {
        this.table = table
        return this
    }

    fun orderBy(column: String, isDesc: Boolean = true): QueryBuilder {
        order = "ORDER BY $column ${if (isDesc) "DESC" else "ASC"}"
        return this
    }

    fun appendWhere(condition: String, logic: String = "AND"): QueryBuilder {
        if (whereCondition.isNullOrEmpty()) whereCondition = "WHERE $condition "
        else whereCondition += "$logic $condition "
        return this
    }

    fun innerJoin(table: String, on: String): QueryBuilder {
        if (joinTables.isNullOrEmpty()) joinTables = "INNER JOIN $table ON $on "
        else joinTables += "INNER JOIN $table ON $on "
        return this
    }

    fun build(): String {
        check(table != null) { "table must be not null" }
        val strBuilder = StringBuilder("SELECT ")
            .append("$selectColumns ")
            .append("FROM $table ")

        if (joinTables != null) strBuilder.append(joinTables)
        if (whereCondition != null) strBuilder.append(whereCondition)
        if (order != null) strBuilder.append(order)
        return strBuilder.toString()
    }


}

