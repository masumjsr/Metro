package com.masum.metro.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masum.metro.data.model.Line
import com.masum.metro.data.model.Route
import com.masum.metro.data.model.Station
import kotlinx.coroutines.flow.Flow

@Dao
interface LinesDao {
    @Transaction
    @Query(
        value = "SELECT * FROM i_lines"
    )
    fun getAllLines(): Flow<List<Line>>
}