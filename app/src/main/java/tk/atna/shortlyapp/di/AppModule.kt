package tk.atna.shortlyapp.di

import androidx.room.Room
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tk.atna.shortlyapp.data.datasource.api.ApiExceptionCallAdapterFactory
import tk.atna.shortlyapp.data.datasource.api.ServerApi
import tk.atna.shortlyapp.data.datasource.db.AppDatabase
import tk.atna.shortlyapp.data.repository.UrlsRepositoryImpl
import tk.atna.shortlyapp.domain.interactor.UrlsInteractor
import tk.atna.shortlyapp.domain.repository.UrlsRepository
import tk.atna.shortlyapp.presentation.main.MainViewModel
import java.util.concurrent.TimeUnit

const val CONNECT_TIMEOUT = 60_000L
const val READ_TIMEOUT = 60_000L
const val BASE_URL = "https://api.shrtco.de/v2/"
const val DATABASE_NAME = "shortly_db"

val appModule = module {

    // data base init
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().shortenedUrlsDao() }

    // network init
    single { Gson() }
    single<Converter.Factory> { GsonConverterFactory.create(get()) }
    single<CallAdapter.Factory> { ApiExceptionCallAdapterFactory(get()) }
    single { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }
    single {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(get())
            .addCallAdapterFactory(get())
            .build()
    }
    single<ServerApi> { get<Retrofit>().create(ServerApi::class.java) }

    // other stuff
    single<UrlsRepository> { UrlsRepositoryImpl(get(), get()) }

    factory { UrlsInteractor(get()) }

    viewModel { MainViewModel(get()) }
}