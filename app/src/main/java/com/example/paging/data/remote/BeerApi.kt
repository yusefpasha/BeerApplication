package com.example.paging.data.remote

interface BeerApi {

    suspend fun getBeers(page: Int = 1, perPage: Int = 10): List<BeerDto>
}