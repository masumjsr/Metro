package com.masum.metro.data.repository

import com.masum.metro.data.iface.Api
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val api: Api
) {
    suspend fun networkRequest()=api.getCategory()
}