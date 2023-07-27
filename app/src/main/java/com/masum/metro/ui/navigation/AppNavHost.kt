package com.masum.metro.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier =Modifier,
    startDestination:String = homeNavigationRoute,
    onPickClick :(Boolean)->Unit

) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
         ){
        HomeScreen()
        RouteScreen(){from,to->
            navController.navigateToResult(from,to) }
        ResultScreen(){
            Log.i("123321", "AppNavHost: pop back clicked")
            navController.popBackStack()
        }
        AboutScreen()
        NearbyScreen()
    }

}