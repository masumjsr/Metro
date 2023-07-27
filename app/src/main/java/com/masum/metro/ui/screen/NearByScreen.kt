package com.masum.metro.ui.screen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.masum.metro.data.model.LocationDetails
import com.masum.metro.ui.viewmodel.NearbyViewModel
import com.masum.metro.util.ShowAlertDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NearByScreenRoute() {
NearByScreen()
}


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun NearByScreen(
    viewModel: NearbyViewModel = hiltViewModel(),
) {

    val nearby by viewModel.stations.collectAsStateWithLifecycle()

    Log.i("123321", "NearByScreen: nearby data is $nearby")
    val context = LocalContext.current



    var refreshing by remember { mutableStateOf(true)}
    var isShowDialog by remember { mutableStateOf(false)}


    val permissionState =  rememberMultiplePermissionsState(permissions =  listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    ))
    if (permissionState.allPermissionsGranted) {


        val fusedLocationClient =LocationServices.getFusedLocationProviderClient(context)


        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            .setMaxUpdateDelayMillis(50000)
            .build()

        val settingResultRequest = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                Log.i("123321", "NearByScreen: result function is OK")
                if (permissionState.allPermissionsGranted) {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                        override fun isCancellationRequested() = false
                    })
                        .addOnSuccessListener { location: Location? ->
                            if (location == null)
                                Toast.makeText(context, "Cannot get location.", Toast.LENGTH_SHORT)
                                    .show()
                            else {
                                refreshing=false

                             viewModel.updateLocaton(location.latitude, location.longitude)
                            }
                        }
                }
            }

        }



        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)

        val gpsSettingTask: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())




var intentSenderRequest: IntentSenderRequest? by remember { mutableStateOf(null)}
        if(isShowDialog){
            ShowAlertDialog(
                title = "Disclaimer",
                text ="We need location permission to find the nearby metro station. We do not share or store your location. We  do not collect your  location in Background",
                positiveButton ="Ok" ,
                negativeButton ="" ,
                onDismiss = { }) {
                settingResultRequest.launch(intentSenderRequest)
                isShowDialog = false

            }
        }

        gpsSettingTask.addOnSuccessListener {


            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context
                    ,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                    .addOnSuccessListener { location: Location? ->
                        if (location == null)
                            Toast.makeText(context, "Cannot get location.", Toast.LENGTH_SHORT)
                                .show()
                        else {
                            refreshing=false
                            viewModel.updateLocaton(location.latitude, location.longitude)

                        }
                    }
            }


        }
        gpsSettingTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution)
                        .build()

                    settingResultRequest.launch(intentSenderRequest)

                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(context, sendEx.message, Toast.LENGTH_SHORT).show()
                    // ignore here
                }
            }
        }


        val refreshScope = rememberCoroutineScope()

       

        fun refresh() = refreshScope.launch {

            refreshing = true
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        Toast.makeText(context, "Cannot get location.", Toast.LENGTH_SHORT)
                            .show()
                    else {
                        refreshing=false
                        viewModel.updateLocaton(location.latitude, location.longitude)

                    }
                }
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

        Box(
            Modifier
                .pullRefresh(state)
                .padding(
                    top = 0.dp
                )) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(nearby) {

                    ElevatedCard( modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .padding(
                                    10.dp
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            androidx.compose.material3.Text(text = it.station.name_en)
                            androidx.compose.material3.Text(text = it.distance)
                        }
                    }
                }
            }

           if(refreshing) PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

        }



    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
                ){
            val textToShow = if (permissionState.shouldShowRationale) {

                "We need location permission to find the nearby metro station. We do not share or store your location. We  do not collect your  location in Background"
            } else {

                "We need location permission to find the nearby metro station. We do not share or store your location. We  do not collect your  location in Background"
            }
            androidx.compose.material3.Text(textToShow, textAlign = TextAlign.Center)
            Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                Text("Request permission")
            }
        }
    }













}


@Preview
@Composable
fun previewNearByScreen() {
    NearByScreen()
}