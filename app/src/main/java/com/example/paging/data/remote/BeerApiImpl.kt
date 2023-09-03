package com.example.paging.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class BeerApiImpl(private val client: HttpClient) : BeerApi {

    override suspend fun getBeers(page: Int, perPage: Int) = client.get("beers") {
        parameter("page", page)
        parameter("per_page", perPage)
    }.body<List<BeerDto>>()
}