package com.masum.metro.data.database

import androidx.compose.ui.graphics.PointMode.Companion.Lines
import androidx.room.Database
import androidx.room.RoomDatabase
import com.masum.metro.data.database.dao.LinesDao
import com.masum.metro.data.database.dao.RoutesDao
import com.masum.metro.data.database.dao.StationDao
import com.masum.metro.data.model.Line
import com.masum.metro.data.model.Route
import com.masum.metro.data.model.Station


@Database(
    entities = [
        Station::class,
    Line::class,
    Route::class
    ],
    version =
        1
)
abstract  class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
    abstract fun routeDao(): RoutesDao
    abstract fun lineDao(): LinesDao
}