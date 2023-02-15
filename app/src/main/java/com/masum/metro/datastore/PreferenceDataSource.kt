package com.masum.metro.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.masum.metro.UserPreferences
import com.masum.metro.copy
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>

) {
    val userData = userPreferences.data
        .map {
            UserData(
                ad =it.adConfig
            )
        }
    suspend fun updateTheme(themeId: Int){
        try {
            userPreferences.updateData {
                it.copy {
                   adConfig=themeId

                }
            }
        }
        catch(e: IllegalStateException){

        }
    }

}






/*if (mo4336J(str3, str5, str4).length() > 0) {
    int i8 = 6;
    if (!str2.toLowerCase().equals("ne")) {
        if (!str2.toLowerCase().equals("ew") && !str2.toLowerCase().equals("cg")) {
            if (str2.toLowerCase().equals("cc") || str2.toLowerCase().equals("ce")) {
                i8 = 4;
            } else if (!str2.toLowerCase().equals("ns") && !str2.toLowerCase().equals("dt")) {
                if (str2.toLowerCase().equals("te")) {
                    int i9 = Calendar.getInstance().get(11);
                    i8 = 9;
                    if (i9 >= 7 && i9 <= 9) {
                        i8 = 5;
                    }
                }
            }
        }
        i8 =*/


