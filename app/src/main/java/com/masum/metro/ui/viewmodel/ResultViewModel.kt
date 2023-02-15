package com.masum.metro.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.masum.metro.data.model.*
import com.masum.metro.data.repository.MainRepository
import com.masum.metro.data.repository.UserRepository
import com.masum.metro.ui.navigation.fromArg
import com.masum.metro.ui.navigation.toArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,


    ): ViewModel(){
    val ad =userRepository.userData.map{it.ad}

    val from=savedStateHandle.get<String>(key = "from")?:""
    val to  =savedStateHandle.get<String>(key = toArg)?:""

    val station =mainRepository.getStationList()
    private val line =mainRepository.getLineList()
    private val row_route= mainRepository
        .getRoute(from.split(",").first(),(to.split(",").first()))





    val route:StateFlow<ResultModel> =
        station
            .combine(line){stationList,line->


                stationList.map{ station ->
                    val lineList = ArrayList<Line>()
                    station.lines.toCharArray().forEach {code->
                        lineList.add(line.first { it.code == code.toString() })
                    }
                    StationWithLine(station,lineList)

                }

            }

            .combine(row_route){ station: List<StationWithLine>,it: Route, ->
            val stepList=ArrayList<StepModel>()


            val steps = it.r_route.split(",")
            steps.forEachIndexed { index, s ->
                val match_id =s.replace("+","").toString().replace("-","")
                val startStation = station.first {it.station.exploreid.contains(match_id) }
                stepList.add(
                    StepModel(
                        index,
                        " ${startStation.station.name_en}",
                        color = startStation.lines.first().color,
                        time = "  (with approx. ${startStation.lines.first().wait} min wait )"
                    ))
            }
            val startStation = station.first {it.station.exploreid==to }
            stepList.add(StepModel(steps.size, startStation.station.name_en, color = startStation.lines.first().color))

            ResultModel(
                price = "${it.r_price/100.0}",
                time ="${it.r_time/60}",
                stations =( it.r_route.replace('+','-').count { it=='-' }).toString(),
                line=(it.r_route.count {  it==','}+1).toString(),

                step = stepList.groupBy { it.title}.entries.map { it.value.maxBy { it.position} }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue =ResultModel("","","","", emptyList())
        )

}