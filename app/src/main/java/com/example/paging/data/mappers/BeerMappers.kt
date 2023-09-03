package com.example.paging.data.mappers

import com.example.paging.data.local.BeerEntity
import com.example.paging.data.remote.BeerDto
import com.example.paging.domain.Beer

fun BeerDto.toBeerEntity() = BeerEntity(
    id = id,
    name = name,
    tagline = tagline,
    description = description,
    firstBrewed = firstBrewed,
    imageUrl = imageUrl,
)

fun BeerEntity.toBeer() = Beer(
    id = id,
    name = name,
    tagline = tagline,
    description = description,
    firstBrewed = firstBrewed,
    imageUrl = imageUrl,
)