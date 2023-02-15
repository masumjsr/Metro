package com.masum.metro.data.repository

import com.masum.metro.data.database.dao.LinesDao
import com.masum.metro.data.database.dao.RoutesDao
import com.masum.metro.data.database.dao.StationDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val stationDao: StationDao,
    private val linesDao: LinesDao,
    private val routesDao: RoutesDao

) {
    fun getStationList()=stationDao.getAllStation()
    fun getLineList( )= linesDao.getAllLines()
    fun getRoute(from:String,to:String)= routesDao.getRoute(from,to)
}