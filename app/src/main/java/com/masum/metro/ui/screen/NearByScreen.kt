package com.masum.metro.ui.screen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.content.pm.PackageManager
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
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

    val context = LocalContext.current



    var refreshing by remember { mutableStateOf(true)}
    var isShowDialog by remember { mutableStateOf(true)}

 if(isShowDialog){
     ShowAlertDialog(
         title = "Disclaimer",
         text = "This app colllects location data to enable 'Search Nearby Metro Location' even when the app is closed or not in use.",
         positiveButton ="Ok" ,
         negativeButton ="" ,
         onDismiss = { }) {
         isShowDialog = false

     }
 }

    val permissionState =  rememberMultiplePermissionsState(permissions =  listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    ))
    if (permissionState.allPermissionsGranted) {


        val fusedLocationClient =LocationServices.getFusedLocationProviderClient(context)
        val  locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (lo in p0.locations) {
                    // Update UI with location data
                    refreshing=false
                    viewModel.location.value = LocationDetails(lo.latitude, lo.longitude)
                }
            }
        }


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
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
           
        }


        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)

        val gpsSettingTask: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())


        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper())
        gpsSettingTask.addOnSuccessListener {

                Log.i("123321", "NearByScreen:  this function is called")

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper())


        }
        gpsSettingTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest
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

        val nearby by viewModel.stations.collectAsStateWithLifecycle()

        fun refresh() = refreshScope.launch {

            refreshing = true
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper())
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

        Box(
            Modifier
                .pullRefresh(state)
                .padding(
                    top = 0.dp
                )) {
            LazyColumn(Modifier.fillMaxSize()) {
                if (!refreshing) {
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

                "The Location is important for this app. Please grant the permission."
            } else {

                "Location permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            androidx.compose.material3.Text(textToShow)
            Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                Text("Request permission")
            }
        }
    }



   val fusedLocationClient =LocationServices.getFusedLocationProviderClient(context)
   val  locationCallback = object : LocationCallback() {
       override fun onLocationAvailability(p0: LocationAvailability) {
           super.onLocationAvailability(p0)

           Log.i("123321", "onLocationAvailability: ")
       }
        override fun onLocationResult(p0: LocationResult) {
            Log.i("123321", "onLocationResult: $p0")
            for (lo in p0.locations) {
                // Update UI with location data
               viewModel.location.value = LocationDetails(lo.latitude, lo.longitude)
            }
        }
    }

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->

        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        Log.i("123321", "NearByScreen: are granted=$areGranted" +
                "")
        if (areGranted) {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }








}


@Preview
@Composable
fun previewNearByScreen() {
    NearByScreen()
}