package com.duckbuddyy.nasarest.api

import com.duckbuddyy.nasarest.BuildConfig
import com.duckbuddyy.nasarest.domain.CameraType
import com.duckbuddyy.nasarest.domain.RoverType
import retrofit2.http.*

interface NasaApi {

    companion object{
        const val API_KEY = BuildConfig.NASA_API_KEY
        const val BASE_URL = "https://api.nasa.gov/"
    }

    @GET("mars-photos/api/v1/rovers/{rover}/photos")
    suspend fun searchPhotos(
        @Path("rover") rover:RoverType,
        @Query("page") page: Int,
        @Query("sol") sol: Int,
        @Query("camera") cameraType: CameraType?,
        @Query("api_key") apiKey: String = API_KEY,
    ): NasaResponse

}