package com.masum.metro.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masum.metro.data.model.Station
import com.masum.metro.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun RouteScreenRoute(
     onResultClick :(String,String)->Unit
) {
    RouteScreen(onResultClick)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    onPickClick: (String, String) -> Unit,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalView.current.context



    /*var from  by remember {
    mutableStateOf(Station("",0.0,0,"","","",0.0))

    }*/

    val from by mainViewModel.fromField.collectAsStateWithLifecycle()
    val to by mainViewModel.toField.collectAsStateWithLifecycle()





    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    var isFrom by remember {
        mutableStateOf(false)
    }
    BackHandler(enabled = modalSheetState.isVisible) {
        coroutineScope.launch {
            modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
        }
    }

    val roundedCornerRadius = 12.dp
    ModalBottomSheetLayout(
        sheetState = modalSheetState,

        sheetShape = RoundedCornerShape(
            topStart = roundedCornerRadius,
            topEnd = roundedCornerRadius
        ),
        sheetContent = {
            StationScreen(onClick = {
                coroutineScope.launch {
                    modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
                }
                if (isFrom) mainViewModel.fromField.value=it
                else mainViewModel.toField.value=it

            },
                onClose = {
                    coroutineScope.launch {
                        modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
                    }
                },
                onFocus = {
                    coroutineScope.launch {
                        modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                    }
                }

                )
        })
    {

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        androidx.compose.material3.Scaffold (
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {

            },
                ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(10.dp)
            ) {
                Text(text = "From")
                TextField(
                    modifier = Modifier
                        .clickable {
                            isFrom = true
                            coroutineScope.launch {
                                modalSheetState.animateTo(ModalBottomSheetValue.HalfExpanded)
                            }

                        }
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = from.name_en ?: "", onValueChange = {}, readOnly = true, enabled = false
                )

                Text(text = "To")
                androidx.compose.material3.TextField(
                    modifier = Modifier
                        .clickable {
                            isFrom = false
                            coroutineScope.launch {
                                modalSheetState.animateTo(ModalBottomSheetValue.HalfExpanded)
                            }
                        }
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = to.name_en ?: "", onValueChange = {}, readOnly = true, enabled = false
                )

                Button(
                    onClick = {
                        if(from.exploreid.isEmpty()) {
                            coroutineScope.launch { // using the `coroutineScope` to `launch` showing the snackbar

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Select Start Destination",
                                    )
                                }
                            }
                        }
                        else if(from.exploreid.isEmpty()) {
                            coroutineScope.launch { // using the `coroutineScope` to `launch` showing the snackbar

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Select End  Destination",
                                    )
                                }
                            }
                        }
                       else onPickClick.invoke(from.exploreid,to.exploreid)
                              },
                    shape = RoundedCornerShape(20),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .padding(8.dp),
                ) {
                    Text(text = "Calculate Route")
                }

            }
        }

    }

}



@Preview
@Composable
fun RouteScreenPreview() {
    RouteScreen(onPickClick = {_,_->})
}