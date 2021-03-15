package ru.skillbranch.skillarticles.di.modules

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.skillbranch.skillarticles.AppConfig
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.NetworkMonitor
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.remote.adapters.DateAdapter
import ru.skillbranch.skillarticles.data.remote.interceptors.ErrorStatusInterceptor
import ru.skillbranch.skillarticles.data.remote.interceptors.NetworkStatusInterceptor
import ru.skillbranch.skillarticles.data.remote.interceptors.TokenAuthenticator
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(client) //set http client
        .addConverterFactory(MoshiConverterFactory.create(moshi)) //set json converter/parser
        .baseUrl(AppConfig.BASE_URL)
        .build()

    @Provides
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authenticator: TokenAuthenticator,
        statusInterceptor: NetworkStatusInterceptor,
        errStatusInterceptor: ErrorStatusInterceptor): OkHttpClient =
        OkHttpClient().newBuilder()
            .readTimeout(2, TimeUnit.SECONDS) //socket timeout (GET)
            .writeTimeout(5, TimeUnit.SECONDS) //socket timeout (POST , PUT, etc)
            .authenticator(authenticator)
            .addInterceptor(statusInterceptor) //intercept network status
            .addInterceptor(logging) //intercept req/res for logging
            .addInterceptor(errStatusInterceptor) //intercept status errors
            .build()

    @Provides
    fun provideRestService(retrofit: Retrofit): RestService =
        retrofit.create(RestService::class.java)

    @Provides
    fun provideNetworkStatusInterceptor(monitor: NetworkMonitor): NetworkStatusInterceptor =
        NetworkStatusInterceptor(monitor)

    @Provides
    fun provideErrorStatusInterceptor(moshi: Moshi): ErrorStatusInterceptor =
        ErrorStatusInterceptor(moshi)

    @Provides
    fun provideTokenAuthenticator(prefManager: PrefManager,
                                  lazyApi: Lazy<RestService>): TokenAuthenticator =
        TokenAuthenticator(prefManager, lazyApi)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(DateAdapter()) //convert long timestamp to Date
        .add(KotlinJsonAdapterFactory()) //convert json to class by reflection
        .build()
}