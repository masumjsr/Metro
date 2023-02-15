package com.masum.metro.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masum.metro.data.model.Station
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Transaction
    @Query(
        value = "SELECT * FROM i_stations"
    )
    fun getAllStation(): Flow<List<Station>>
}