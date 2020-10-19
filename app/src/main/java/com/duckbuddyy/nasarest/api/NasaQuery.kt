package com.duckbuddyy.nasarest.api

import com.duckbuddyy.nasarest.domain.CameraType
import com.duckbuddyy.nasarest.domain.RoverType

data class NasaQuery(
    val sol: Int,
    val cameraType: CameraType?,
    val rover: RoverType
)