package com.example.paging.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path

interface BeerApi {

    suspend fun test() = client.get("") {}

    suspend fun getBeers(page: Int, perPage: Int) = client.get("beers") {
        //url("beers")
        parameter("page", page)
        parameter("per_page", perPage)
    }.body<List<BeerDto>>()

    companion object {

        private const val baseUrl = "https://api.punkapi.com/v2/"

        val client = HttpClient(Android) {
            Logging {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
            engine {
                socketTimeout = 15_000
                connectTimeout = 30_000
            }
            defaultRequest {
                url(baseUrl)
                url {
                    protocol = if (baseUrl.substringBefore("://").lowercase().contains("https")) {
                        URLProtocol.HTTPS
                    } else {
                        URLProtocol.HTTP
                    }
                    host = baseUrl.substringAfter("://").substringBefore("/")
                    path(baseUrl.substringAfter(host))
                }
            }
        }
    }
}