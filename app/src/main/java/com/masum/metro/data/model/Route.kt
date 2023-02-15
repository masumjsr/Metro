package com.masum.metro.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "i_routes")
data class Route (
    @PrimaryKey(autoGenerate = false)
    val _id:Int,
    val r_from:Int,
    val r_to:Int,
    val r_price:Int,
    val r_time:Int,
    val r_route:String
)