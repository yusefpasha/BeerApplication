package com.example.paging.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paging.data.local.BeerDatabase
import com.example.paging.data.local.BeerEntity
import com.example.paging.data.mappers.toBeerEntity
import okio.IOException

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(private val beerDb: BeerDatabase, private val beerApi: BeerApi) : RemoteMediator<Int, BeerEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, BeerEntity>): MediatorResult {
        return try {
            val loadKey: Int = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val beers = beerApi.getBeers(page = loadKey, perPage = state.config.pageSize)

            // Because we Need to Multiple Transaction in same time, use Room Transaction
            beerDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    beerDb.dao.clearAll()
                }
                val beerEntities = beers.map { it.toBeerEntity() }
                beerDb.dao.upsertAll(beerEntities)
            }

            MediatorResult.Success(endOfPaginationReached = beers.isEmpty())
        } catch (e: IOException) {
            Log.d("PAGING", "1 -> " + e.stackTraceToString())
            MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.d("PAGING", "2 -> " + e.stackTraceToString())
            MediatorResult.Error(e)
        }
    }
}