package com.masum.metro.ui.screen

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.masum.metro.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val ad = viewModel.ad.collectAsStateWithLifecycle(initialValue = 0)
    
    val context = LocalView.current.context

    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AndroidView(factory = {
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.builtInZoomControls = true;
                loadUrl("file:///android_asset/map.html")
            }
        })


    }
}