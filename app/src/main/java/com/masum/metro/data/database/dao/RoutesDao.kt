package com.masum.metro.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masum.metro.data.model.Route
import com.masum.metro.data.model.Station
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutesDao {
    @Transaction
    @Query(
        value = "SELECT * FROM i_routes where r_from=:from and r_to=:to"
    )
    fun getRoute(from:String,to:String): Flow<Route>
}