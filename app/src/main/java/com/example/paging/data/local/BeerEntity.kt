package com.example.paging.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.paging.utils.Constants

@Entity(tableName = Constants.DB_BEER_TABLE)
data class BeerEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val tagline: String,
    val description: String,
    @ColumnInfo(name = "first_brewed")
    val firstBrewed: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
)
