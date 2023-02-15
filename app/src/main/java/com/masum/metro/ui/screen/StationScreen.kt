package com.masum.metro.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masum.metro.data.model.Station
import com.masum.metro.ui.component.StationCard
import com.masum.metro.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationScreen(
    onClick:(Station)->Unit,
    onFocus:()->Unit,
    onClose:()->Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val  stations by viewModel.stations.collectAsStateWithLifecycle()
    val searchText by viewModel.searchTextState.collectAsState()
    
    Scaffold { padding ->
        Column (horizontalAlignment = Alignment.CenterHorizontally){
                Divider(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(0.50f),
                            RoundedCornerShape(50)
                        )
                        .padding(5.dp)
                        .height(3.dp)
                        .width(40.dp)
                )

            androidx.compose.material3.TextField(
                value = searchText,
                onValueChange = { onQueryChanged ->
                    viewModel.updateSearchTextState(onQueryChanged)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Search icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        onClose .invoke()
                        viewModel.updateSearchTextState("")
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = "Clear icon"
                        )
                    }
                },
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, textColor = Color.Unspecified),
                placeholder = { Text(text = "Search...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { if (it.isFocused) onFocus.invoke() }
            )

            LazyColumn(modifier = Modifier.padding(padding)) {

                items(stations) {
                    StationCard(stationWithLine = it) {
                        viewModel.updateSearchTextState("")
                        onClick.invoke(it)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun previewStation() {
    StationScreen(onClick = {}, onFocus = { /*TODO*/ }, onClose = { /*TODO*/ })
}