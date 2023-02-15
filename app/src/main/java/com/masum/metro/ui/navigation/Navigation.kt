package com.masum.metro.ui.navigation

import androidx.annotation.VisibleForTesting
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.masum.metro.ui.screen.*

const val homeNavigationRoute = "home_navigation_route"
const val journeyNavigationRoute = "journey_navigation_route"
const val aboutNavigationRoute = "about_navigation_route"
const val resultNavigationRoute = "result_navigation_route"
const val nearbyNavigationRoute = "nearby_navigation_route"

@VisibleForTesting
internal const val fromArg ="from"
internal const val toArg ="to"


fun NavController.navigateToHome(navOptions: NavOptions? = null){
    this.navigate(homeNavigationRoute,navOptions)
}


fun NavController.navigateToJourney(navOptions: NavOptions? = null){
    this.navigate(journeyNavigationRoute,navOptions)
}

fun NavController.navigateToAbout(navOptions: NavOptions? = null){
    this.navigate(aboutNavigationRoute,navOptions)
}

fun NavController.navigateToNearby(navOptions: NavOptions? = null){
    this.navigate(nearbyNavigationRoute,navOptions)
}

fun NavGraphBuilder.HomeScreen(){
    composable(homeNavigationRoute){
    MainScreen()
    }

}

fun NavGraphBuilder.AboutScreen(){
    composable(aboutNavigationRoute){
   AboutScreenRoute()
    }
}

fun NavGraphBuilder.NearbyScreen(){
    composable(nearbyNavigationRoute){
   NearByScreenRoute()
    }
}


fun NavGraphBuilder.RouteScreen(
    onPickClick :(String,String)->Unit

){
    composable(journeyNavigationRoute){
        RouteScreenRoute(onPickClick)
    }
}

fun NavGraphBuilder.ResultScreen(onBackClick:()->Unit){
    composable(route="$resultNavigationRoute/{$fromArg}/{$toArg}",
        arguments = listOf(
            navArgument(fromArg){type = NavType.StringType},
            navArgument(toArg){type = NavType.StringType}
        )
    ){
        ResultScreenRoute(onBackClick)
    }
}
fun NavController.navigateToResult(from:String, to:String){
    this.navigate("$resultNavigationRoute/$from/$to")
}