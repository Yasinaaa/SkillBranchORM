package ru.skillbranch.skillarticles.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.repositories.IRepository
import ru.skillbranch.skillarticles.data.repositories.RootRepository
import ru.skillbranch.skillarticles.di.scopes.ActivityScope
import ru.skillbranch.skillarticles.example.TestActivity
import ru.skillbranch.skillarticles.example.TestViewModel
import ru.skillbranch.skillarticles.example.TestViewModelFactory
import javax.inject.Singleton

@Module
abstract class ActivityModule {

    @ActivityScope
    @Provides
    fun providePair(): Pair<String, String> = "inject" to "pair"

    @ActivityScope
    @Binds
    abstract fun bindRootRepository(
        repository: RootRepository
    ): IRepository

    abstract fun bindVmFactory(factory: TestViewModelFactory): ViewModelProvider.Factory

//    fun bindRootRepository(
//        preferences: PrefManager,
//        network: RestService
//    ): RootRepository = RootRepository(preferences, network)

    //IF NEED OTHER REPOSITORY
    //    @ActivityScope
    //    @Provides
    //    fun provideRootRepository(
    //        preferences: PrefManager,
    //        network: RestService
    //    ): ArticlesRepository = ArticlesRepository(preferences, network)
    //    @ActivityScope
    //    @Provides
    //    fun provideRootRepository(
    //        preferences: PrefManager,
    //        network: RestService
    //    ): ArticleRepository = ArticleRepository(preferences, network)
    //etc

//    @ActivityScope
//    @Provides
//    fun provideTestViewModel(repository: RootRepository): TestViewModel =
//        TestViewModel(repository)

    @Binds
    @ActivityScope
    abstract fun bindViewModel(vm: TestViewModel): ViewModel

    //IF NEED OTHER VM
    //    @ActivityScope
    //    @Provides
    //    fun provideTestViewModel(repository: ArticlesRepository): ArticlesViewModel =
    //            ArticlesViewModel(repository)

    //    @ActivityScope
    //    @Provides
    //    fun provideTestViewModel(repository: ArticleRepository): ArticleViewModel =
    //            ArticleViewModel(repository)
    //etc

//    @Provides
//    fun provideTestActivity(activity: TestActivity): TestActivity = activity
}