package com.masum.metro.data.repository

import com.masum.metro.datastore.PreferenceDataSource
import com.masum.metro.datastore.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource
) {
    val userData : Flow<UserData> = preferenceDataSource.userData

    suspend fun updateAd(id:Int)=preferenceDataSource.updateTheme(id)



}
