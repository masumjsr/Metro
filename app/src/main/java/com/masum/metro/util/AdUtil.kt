package com.masum.metro.util

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.masum.metro.BuildConfig
import com.masum.metro.R
import com.masum.metro.ui.screen.mInterstitialAd

 val AD_UNIT_ID =if(!BuildConfig.DEBUG) "ca-app-pub-7132529534932682/7401506275" else "ca-app-pub-3940256099942544/3419835294"
 val ADMOB_INTERSTITIAL_ID =if(!BuildConfig.DEBUG) "ca-app-pub-7132529534932682/9195259660" else "ca-app-pub-3940256099942544/1033173712"
 val ADMOB_NATIVE_AD_ID = if(!BuildConfig.DEBUG)"ca-app-pub-7132529534932682/1027669617" else "ca-app-pub-3940256099942544/2247696110"
 val ADMOB_BANNER_AD_ID = if(!BuildConfig.DEBUG)"ca-app-pub-7132529534932682/5301465273" else "ca-app-pub-3940256099942544/6300978111"



fun loadInterstitial(context: Context) {
    Log.i("123321", "loadInterstitial: loading admob ad")
    InterstitialAd.load(
        context,
        //"ca-app-pub-3940256099942544/1033173712", //Change this with your own AdUnitID!
       ADMOB_INTERSTITIAL_ID,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.i("123321", "onAdFailedToLoad: reason :${adError.message}")
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        }
    )
}
fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun showInterstitial(context: Context, onAdDismissed: () -> Unit) {
    Log.i("123321", "showInterstitial: showinterstitial")
    val activity = context.findActivity()

    if (mInterstitialAd != null && activity != null) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(e: AdError) {

                onAdDismissed()
                mInterstitialAd = null
            }

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null

                onAdDismissed()
            }
        }
        mInterstitialAd?.show(activity)
    }
    else

        onAdDismissed()

}

@Composable
 fun AdmobBanner() {
    val context= LocalView.current.context
    AndroidView(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        factory = { context ->

            val view = LayoutInflater.from(context).inflate(R.layout.ad_layout, null, false)



            // do whatever you want...
            view // return the view
        },
        update = { view ->

            val adLoader: AdLoader = AdLoader.Builder(context, ADMOB_NATIVE_AD_ID)
                .forNativeAd { nativeAd ->

                    val styles =
                        NativeTemplateStyle.Builder()
                            .build()
                    val template: TemplateView = view.findViewById(R.id.my_template)
                    template.setStyles(styles)
                    template.setNativeAd(nativeAd)
                }
                .build()

            adLoader.loadAd(AdRequest.Builder().build())

            // Update the view

        }
    )
}