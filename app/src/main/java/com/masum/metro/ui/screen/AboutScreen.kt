package com.masum.metro.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.masum.metro.BuildConfig
import com.masum.metro.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreenRoute() {
    Scaffold(

    ) {
        val context = LocalView.current.context
        Column(modifier = Modifier
            .padding(it)
            .fillMaxWidth()
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.mipmap.ic_launcher_foreground), contentDescription = "logo")
            Text(text = " MRT & LRT Map", fontWeight = FontWeight.Bold, fontSize = 30.sp)
            Text(text = "Version: ${BuildConfig.VERSION_NAME}", fontSize = 16.sp)
            Text(text = "Copyright (c) Su soft inc.", modifier = Modifier.padding(10.dp))
            Button(onClick = {
                val url = "https://sgmrtlrtroutmap.blogspot.com/2023/02/privacy-policy.html"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                context.startActivity(i)
            }) {
                Text(text = "Privacy Policy", modifier = Modifier.padding(5.dp))
            }
        }

    }

}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreenRoute()
}