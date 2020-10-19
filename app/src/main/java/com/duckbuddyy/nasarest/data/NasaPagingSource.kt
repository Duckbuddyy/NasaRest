package com.duckbuddyy.nasarest.data

import android.util.Log
import androidx.paging.PagingSource
import com.duckbuddyy.nasarest.api.NasaApi
import com.duckbuddyy.nasarest.api.NasaQuery
import com.duckbuddyy.nasarest.domain.CameraType
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE_INDEX = 1

class NasaPagingSource(
    private val nasaApi: NasaApi,
    private val nasaQuery: NasaQuery,
) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: START_PAGE_INDEX

        return try {
            val response = nasaApi.searchPhotos(
                rover = nasaQuery.rover,
                page = position,
                sol= nasaQuery.sol,
                cameraType = if(nasaQuery.cameraType == CameraType.ALL) null else nasaQuery.cameraType,
            )

            val photos = response.photos

            LoadResult.Page(
                data = photos,
                prevKey = if (position == START_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1,
            )

        } catch (exception:IOException) {
            LoadResult.Error(exception)
        } catch (exception:HttpException){
            LoadResult.Error(exception)
        }
    }
}