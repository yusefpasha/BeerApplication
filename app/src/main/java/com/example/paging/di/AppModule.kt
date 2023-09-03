package com.example.paging.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.paging.MainViewModel
import com.example.paging.data.local.BeerDatabase
import com.example.paging.data.remote.BeerApi
import com.example.paging.data.remote.BeerApiImpl
import com.example.paging.data.remote.BeerRemoteMediator
import com.example.paging.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
val beerModule = module {

    single {
        Room.databaseBuilder(androidApplication(), BeerDatabase::class.java, Constants.DB_NAME).build()
    }

    single {
        val baseUrl = "https://api.punkapi.com/v2/"

        HttpClient(Android) {

            Logging {
                logger = Logger.ANDROID
                level = LogLevel.ALL

            }

            engine {
                socketTimeout = 15_000
                connectTimeout = 30_000
            }

            install(ContentNegotiation) {
                json(
                    json = Json { ignoreUnknownKeys = true },
                )
            }

            defaultRequest {
                url(baseUrl)
//                url {
//                    protocol = URLProtocol.HTTPS
//                    host = baseUrl.substringAfter("://").substringBefore("/")
//                    path(baseUrl.substringAfter(host))
//                }
            }
        }
    }

    singleOf(::BeerApiImpl) { bind<BeerApi>() }

    single {
        Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = BeerRemoteMediator(beerDb = get(), beerApi = get()),
            pagingSourceFactory = { get<BeerDatabase>().dao.pagingSource() }
        )
    }

    // Main
    viewModelOf(::MainViewModel)
}