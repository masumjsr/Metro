package com.masum.metro.data.model

data class ResultModel(
    val price:String,
    val time:String,
    val stations:String,
    val line:String,

    val step:List<StepModel>
)
