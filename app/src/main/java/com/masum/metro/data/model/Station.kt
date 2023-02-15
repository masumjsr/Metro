package com.masum.metro.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "i_stations")
data class Station(

    val code:String,
    val lng:Double,
    @PrimaryKey(autoGenerate = false)
    val _id:Int,
    val exploreid:String,
    var name_en:String,
    val lines:String,
    val lat:Double,
)