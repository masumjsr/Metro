package com.masum.metro.data.iface


import com.masum.metro.datastore.UserData
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @POST("metro.json")
    suspend fun getCategory(): Response<UserData>




}