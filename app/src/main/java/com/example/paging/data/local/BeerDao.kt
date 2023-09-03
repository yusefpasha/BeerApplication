package com.example.paging.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.paging.utils.Constants

@Dao
interface BeerDao {

    @Upsert
    suspend fun upsertAll(beers: List<BeerEntity>)

    @Query("DELETE FROM ${Constants.DB_BEER_TABLE}")
    suspend fun clearAll()

    @Query("SELECT * FROM ${Constants.DB_BEER_TABLE}")
    fun pagingSource(): PagingSource<Int, BeerEntity>
}