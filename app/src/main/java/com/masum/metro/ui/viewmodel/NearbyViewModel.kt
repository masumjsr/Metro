package com.masum.metro.ui.viewmodel

import android.icu.number.NumberFormatter
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masum.metro.data.model.*
import com.masum.metro.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NearbyViewModel @Inject constructor(
    private val mainRepository: MainRepository,

): ViewModel(){


    val location = MutableStateFlow(LocationDetails(0.0,0.0))


    val stations:StateFlow<List<NearByStation>> = mainRepository.getStationList()

        .combine(location){stations,search->
          val raw= stations.map {
            val location1 = Location("")
            location1.latitude=search.latitude
            location1.longitude=search.longitude

            val location2 = Location("")
            location2.latitude=it.lat
            location2.longitude=it.lng
               val distance = location1.distanceTo(location2)

               NearByStation(it,"${String.format("%.2f",distance/1000.0)} km")
           }
            raw.sortedBy { it.distance }


        }


        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}