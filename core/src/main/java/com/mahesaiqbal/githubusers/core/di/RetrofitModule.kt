package com.mahesaiqbal.githubusers.core.di

import com.mahesaiqbal.githubusers.core.repository.MainRepository
import com.mahesaiqbal.githubusers.core.retrofit.Service
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val GITHUB_URL = "https://api.github.com/"

val retrofitModule = module {

    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    fun provideHttpClient(httpInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpInterceptor)
            .build()
    }

    fun retrofitBuilder(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GITHUB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun provideRetrofit(retrofit: Retrofit) = retrofit.create(Service::class.java)

    fun provideRepository(service: Service) = MainRepository(service)

    // General Koin Single
    single { provideHttpLoggingInterceptor() }
    single { provideHttpClient(get()) }

    // Retrofit Koin Single
    single { retrofitBuilder(get()) }
    single { provideRetrofit(get()) }

    // Repository Koin Single
    single { provideRepository(get()) }

    //==================================================
    //=====================TUTORIAL=====================
    //==================================================
    //more info: https://doc.insert-koin.io/#/koin-android/start

    // =============REGULAR DI===============
    // single{} -> 1 instance per Application
    // factory{} -> 1 instance per Activity

    // retrieving instance:
    // override val service: Service by inject()
    // val service : Service = get()
    // ======================================

    // ===========VIEWMODEL DI===============
    // viewModel{} -> factory{} for ViewModel

    // retrieving instance:
    // activity:
    //    +-------> private val viewModel: ViewModel by viewModel()

    // fragment:
    //    +-------> private val viewModel: ViewModel by viewModel() // instance every fragment
    //    +-------> private val viewModel: ViewModel by sharedViewModel() // shared instance
}