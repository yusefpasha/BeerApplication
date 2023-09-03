package com.example.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.paging.data.local.BeerEntity
import com.example.paging.data.mappers.toBeer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.plus

class MainViewModel(pager: Pager<Int, BeerEntity>) : ViewModel() {

    val beerPagingFlow = pager
        .flow
        .map { pagingData -> pagingData.map { it.toBeer() } }
        .cachedIn(viewModelScope + Dispatchers.IO)
}