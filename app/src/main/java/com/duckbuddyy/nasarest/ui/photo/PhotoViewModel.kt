package com.duckbuddyy.nasarest.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.duckbuddyy.nasarest.api.NasaQuery
import com.duckbuddyy.nasarest.data.NasaRepository
import com.duckbuddyy.nasarest.domain.CameraType
import com.duckbuddyy.nasarest.domain.RoverType

class PhotoViewModel @ViewModelInject constructor(
    private val repository: NasaRepository,

) : ViewModel() {
    val currentQuery = MutableLiveData(DEFAULT_NASA_QUERY)

    companion object {
        private val DEFAULT_NASA_QUERY = NasaQuery(1000, null, RoverType.Curiosity)
    }

    fun searchPhotos(sol: Int, cameraType: CameraType?, rover: RoverType) {
        currentQuery.value = NasaQuery(sol,cameraType,rover)
    }

    val photos = currentQuery.switchMap { nasaQuery ->
        repository.getPhotos(nasaQuery).cachedIn(viewModelScope)
    }
}