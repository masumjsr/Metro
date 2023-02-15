package com.masum.metro.util

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.facebook.ads.Ad
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.masum.metro.ui.screen.facebookInterstitialAd
import com.masum.metro.ui.screen.mInterstitialAd

fun loadFacebookInterstitial(context: Context, onAdDismissed: () -> Unit){
    facebookInterstitialAd = com.facebook.ads.InterstitialAd(context,"2735586973212242_5572919086145669")

    Log.i("123321", "loadFacebookInterstitial: loading ad")
    facebookInterstitialAd?.loadAd(
        facebookInterstitialAd?.buildLoadAdConfig()
            ?.withAdListener(object : InterstitialAdListener {
                override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
                    Log.i("123321", "onError: ")
                }

                override fun onAdLoaded(p0: Ad?) {
                    Log.i("123321", "onAdLoaded: ")
                }

                override fun onAdClicked(p0: Ad?) {
                }

                override fun onLoggingImpression(p0: Ad?) {
                }

                override fun onInterstitialDisplayed(p0: Ad?) {
                }

                override fun onInterstitialDismissed(p0: Ad?) {
                    Log.i("123321", "onInterstitialDismissed: ")
                    onAdDismissed.invoke()

                }
            })
            ?.build()
    )
}
fun showFacebookInterstitial(context: Context){
    run {
        val activity = context.findActivity()

        if (facebookInterstitialAd != null && facebookInterstitialAd?.isAdLoaded == true) {
            facebookInterstitialAd?.show()
        }
    }
}

fun loadInterstitial(context: Context) {
    InterstitialAd.load(
        context,
        "ca-app-pub-7132529534932682/5415048983", //Change this with your own AdUnitID!
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
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
 fun FacebookBanner() {
    AndroidView(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        factory = { context ->
            AdView(
                context, "2735586973212242_5572916946145883",
                AdSize.BANNER_HEIGHT_50
            ).apply {

                loadAd()
            }
        }
    )
}

@Composable
 fun AdmobBanner() {
    AndroidView(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        factory = { context ->
            com.google.android.gms.ads.AdView(context).apply {
                setAdSize(com.google.android.gms.ads.AdSize.BANNER)
                // Add your adUnitID, this is for testing.
                adUnitId = "ca-app-pub-7132529534932682/6728130659"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}