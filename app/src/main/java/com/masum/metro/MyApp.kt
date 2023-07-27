package com.masum.metro



import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.masum.metro.util.AD_UNIT_ID
import dagger.hilt.android.HiltAndroidApp
import java.util.Arrays
import java.util.Date

private const val LOG_TAG = "123321"

/** Application class that initializes, loads and show ads when activities change states. */

@HiltAndroidApp
class MyApplication :
    MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        CaocConfig.Builder.create()
            .errorActivity(CustomErrorActivity::class.java)
            .apply()


        // Log the Mobile Ads SDK version.
        Log.d(LOG_TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion())

        RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("0B7CAF51CFF07FF147EE0C730E31AC21"))
        MobileAds.initialize(this) {}
        AppOpenAdManager(this, AD_UNIT_ID)
    }
}