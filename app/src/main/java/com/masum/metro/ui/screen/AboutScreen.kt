package com.masum.metro.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.masum.metro.util.ShowAlertDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreenRoute() {
    Scaffold(

    ) {
        val context = LocalView.current.context
        var isShowDialog by remember { mutableStateOf(false) }

        if(isShowDialog){
            ShowAlertDialog(
                title = "Privacy Policy",
                text = "Your privacy is important to us. MS Soft inc. as an individual developer build the SG MRT & LRT Route MAP\n" +
                        "application and as an Ad supported application.\n" +
                        "The application uses your's phone GPS/Location\n" +
                        "or current location to improve the application experience.\n" +
                        "- SG MRT & LRT Route MAP application is NOT an official\n" +
                        "app.\n" +
                        "- The application neither shared your location nor stored in the application.\n" +
                        "- We won't use the info to identify or contact you.\n" +
                        "- All data (metro information) are taken from\n" +
                        "'Wikipedia' and official website and are in the public domain.\n" +
                        "Application used Network connection to show the\n" +
                        "\n" +
                        "Metro routes on the Map only.\n" +
                        "\n" +
                        "- Application does not send any user data to anyone\n" +
                        "\n" +
                        "online/offline.\n" +
                        "\n" +
                        "- In case, if you want more details about the\n" +
                        "\n" +
                        "application, please feel free to contact us at\n" +
                        "\n" +
                        "softmsinc@gmail.com",
                positiveButton ="Ok" ,
                negativeButton ="" ,
                onDismiss = { }) {
                isShowDialog = false

            }
        }
        Column(modifier = Modifier
            .padding(it)
            .fillMaxWidth()
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.mipmap.ic_launcher_foreground), contentDescription = "logo")
            Text(text = " MRT & LRT Map", fontWeight = FontWeight.Bold, fontSize = 30.sp)
            Text(text = "Version: ${BuildConfig.VERSION_NAME}", fontSize = 16.sp)
            Text(text = "Copyright (c) Su soft inc.", modifier = Modifier.padding(10.dp))
            Button(onClick = {
                /*val url = "https://masumhossain.xyz/policy.html"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                context.startActivity(i)*/
                isShowDialog=true
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