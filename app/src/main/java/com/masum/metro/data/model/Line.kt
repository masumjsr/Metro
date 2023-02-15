package com.masum.metro.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "i_lines")
data class Line(
    @PrimaryKey(autoGenerate = false)
    val _id:Int,
    val code:String,
    val name_en:String,
    val color:String,
    val legend: String,
    val wait:Int

)