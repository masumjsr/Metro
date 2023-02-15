package com.masum.metro.data.database.modules

import com.masum.metro.data.database.AppDatabase
import com.masum.metro.data.database.dao.LinesDao
import com.masum.metro.data.database.dao.RoutesDao
import com.masum.metro.data.database.dao.StationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideStationDao(
        database: AppDatabase
    ):StationDao=database.stationDao()

    @Provides
    fun provideLineDao(
        database: AppDatabase
    ):LinesDao=database.lineDao()

    @Provides
    fun provideRouteDao(
        database: AppDatabase
    ):RoutesDao=database.routeDao()

}