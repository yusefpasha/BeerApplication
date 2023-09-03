package com.example.paging.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BeerDto(
    val id: Int,
    val name: String,
    val tagline: String,
    @SerialName(value = "first_brewed")
    val firstBrewed: String,
    val description: String,
    @SerialName(value = "image_url")
    val imageUrl: String?,
)
