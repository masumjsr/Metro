package com.masum.metro.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masum.metro.data.model.Line
import com.masum.metro.data.model.Station
import com.masum.metro.data.model.StationWithLine
import com.masum.metro.data.repository.MainRepository
import com.masum.metro.data.repository.NetworkRepository
import com.masum.metro.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val userRepository: UserRepository,
    private val networkRepository: NetworkRepository

): ViewModel(){

     val searchTextState: MutableStateFlow<String> =
        MutableStateFlow("")

    fun updateSearchTextState(newValue: String) {
        searchTextState.value = newValue
    }
    val fromField = MutableStateFlow(Station("",0.0,0,"","","",0.0))
    val toField = MutableStateFlow(Station("",0.0,0,"","","",0.0))

    val ad =userRepository.userData.map{it.ad}


    private val line =mainRepository.getLineList()
    val stations:StateFlow<List<StationWithLine>> = mainRepository.getStationList()

        .combine(searchTextState){stations,search->
            if(search.isNotEmpty())stations.filter { it.name_en.contains(search,true) }
            else stations

        }
        .combine(line){stationList,line->


            stationList.map{ station ->
                val lineList = ArrayList<Line>()
                station.lines.toCharArray().forEach {code->
                    lineList.add(line.first { it.code == code.toString() })
                }
                StationWithLine(station,lineList)

            }

        }

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )



     fun getNetworkRequest(){
        viewModelScope.launch {
            val response = networkRepository.networkRequest()
            if(response.isSuccessful){
                response.body()?.let {
                    userRepository.updateAd(it.ad)
                }
            }
        }
    }
}