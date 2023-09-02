package com.example.paging.data.remote

data class BeerDto(
    val id: Int,
    val name: String,
    val tagline: String,
    val description: String,
    val firstBrewed: String,
    val imageUrl: String?,
)
