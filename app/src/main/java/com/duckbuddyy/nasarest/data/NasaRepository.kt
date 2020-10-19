package com.duckbuddyy.nasarest.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.duckbuddyy.nasarest.api.NasaApi
import com.duckbuddyy.nasarest.api.NasaQuery
import javax.inject.Inject
import javax.inject.Singleton

const val API_PAGE_SIZE = 25

@Singleton
class NasaRepository @Inject constructor(private val nasaApi: NasaApi) {
    fun getPhotos(nasaQuery: NasaQuery) = Pager(
        config = PagingConfig(
            pageSize = API_PAGE_SIZE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            NasaPagingSource(nasaApi,nasaQuery)
        }
    ).liveData
}