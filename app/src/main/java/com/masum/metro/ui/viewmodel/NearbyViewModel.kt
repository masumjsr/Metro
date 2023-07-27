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
import com.masum.metro.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val userRepository: UserRepository,


    ): ViewModel(){


    val location = userRepository.userData
    fun updateLocaton(lat:Double,lon:Double){
        viewModelScope.launch {
            userRepository.updateLocation(lat,lon)
        }
    }



    val stations:StateFlow<List<NearByStation>> = mainRepository.getStationList()

        .combine(location){stations,search->
            Log.i("123321", "search response:$search ")
        if(search.lat==0.0){
            emptyList()
        }
            else {
            val raw= stations.map {
                val location1 = Location("")
                location1.latitude=search.lat
                location1.longitude=search.lon

                val location2 = Location("")
                location2.latitude=it.lat
                location2.longitude=it.lng
                val distance = location1.distanceTo(location2)

                NearByStation(it,"${String.format("%.2f",distance/1000.0)} km")
            }
            raw.sortedBy { it.distance }


        }
        }


        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}