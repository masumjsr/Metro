package com.masum.metro.util

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

fun fromHex(color: String)=Color(android.graphics.Color.parseColor("#$color"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAlertDialog(
    title:String,
    text:String,
    positiveButton:String,
    negativeButton:String,
    onDismiss: () -> Unit,
    onAllowClick: () -> Unit,
) {

    AlertDialog(onDismissRequest = {},

        title = {
            Text(title)
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Text(
                modifier = Modifier
                    .clickable {
                        onAllowClick.invoke()
                    },
                text = positiveButton)
        },

        dismissButton = {
            Text(
                modifier = Modifier
                    .clickable {
                        onDismiss.invoke()
                    },
                text=negativeButton)
        }
    )
}

