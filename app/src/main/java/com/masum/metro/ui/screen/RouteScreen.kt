package com.masum.metro.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masum.metro.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun RouteScreenRoute(
     onResultClick :(String,String)->Unit
) {
    RouteScreen(onResultClick)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
onPickClick: (String, String) -> Unit,
mainViewModel: MainViewModel = hiltViewModel(),
){

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
    )
    val from by mainViewModel.fromField.collectAsStateWithLifecycle()
    val to by mainViewModel.toField.collectAsStateWithLifecycle()






    var isFrom by remember {
        mutableStateOf(true)
    }
    BackHandler(enabled = scaffoldState.bottomSheetState.isVisible) {
        scope.launch {
            scaffoldState.bottomSheetState.partialExpand()
        }
    }



    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            StationScreen(onClick = {
                scope.launch {

                    if(scaffoldState.bottomSheetState.isVisible)
                    scaffoldState.bottomSheetState.partialExpand()

                  //  modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
                }
                if (isFrom) mainViewModel.fromField.value=it
                else mainViewModel.toField.value=it

            },
                onClose = {
                    scope.launch {
                       scaffoldState.bottomSheetState.hide()
                    }
                },
                onFocus = {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }

            )
        }) { innerPadding ->


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(10.dp)
            ) {
                Text(text = "From")
                TextField(
                    modifier = Modifier
                        .clickable {
                            isFrom = true
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }

                        }
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = from.name_en ?: "", onValueChange = {}, readOnly = true, enabled = false
                )

                Text(text = "To")
                TextField(
                    modifier = Modifier
                        .clickable {
                            isFrom = false
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = to.name_en ?: "", onValueChange = {}, readOnly = true, enabled = false
                )

                Button(
                    onClick = {
                        if(from.exploreid.isEmpty()) {
                            scope.launch { // using the `coroutineScope` to `launch` showing the snackbar

                                scope.launch {
                                    scaffoldState.snackbarHostState
                                        .showSnackbar(
                                        "Select Start Destination",
                                    )
                                }
                            }
                        }
                        else if(from.exploreid.isEmpty()) {
                            scope.launch {
                                scaffoldState.snackbarHostState
                                    .showSnackbar(
                                        "Select Start Destination",
                                    )
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


@Preview
@Composable
fun RouteScreenPreview() {
    RouteScreen(onPickClick = {_,_->})
}