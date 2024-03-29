package com.masum.metro

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.masum.metro.ui.AppState
import com.masum.metro.ui.icon.Icon
import com.masum.metro.ui.navigation.AppNavHost
import com.masum.metro.ui.navigation.TopLevelDestination
import com.masum.metro.ui.rememberAppState
import com.masum.metro.ui.screen.StationScreen
import com.masum.metro.ui.theme.MetroTheme
import com.masum.metro.ui.viewmodel.MainViewModel
import com.masum.metro.util.ADMOB_BANNER_AD_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import se.warting.inappupdate.compose.RequireLatestVersion

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()



    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)


        setContent {
            RequireLatestVersion {
                MetroTheme(disableDynamicTheming = false) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val permissionState = rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)

                        if(!permissionState.status.isGranted){
                            SideEffect {
                                permissionState.launchPermissionRequest()
                            }
                        }

                    }
                    // A surface container using the 'background' color from the theme

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        val ad = viewModel.ad.collectAsStateWithLifecycle(initialValue = 0)

                        Column {
                            BannerAds(modifier = Modifier.fillMaxWidth())
                            HomeContent(ad = ad.value)
                        }
                    }
                }
            }
        }
}

}

@Composable
fun BannerAds(
    modifier: Modifier,
    adListener: AdListener? = null
) {

    val deviceCurrentWidth = LocalConfiguration.current.screenWidthDp
    val padding = 1
    var i by remember { mutableStateOf(0) }
    var containerWidth by remember { mutableStateOf<Int?>(null) }

    val context = LocalContext.current
    val adUnitId = ADMOB_BANNER_AD_ID

    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(color = Color.White)
            .onSizeChanged {
                containerWidth = it.width
            },
        contentAlignment = Alignment.Center
    ) {

        Text(text = "Showing Ad")
        AndroidView(factory = {
            AdView(it)
        }, modifier = modifier, update = {
            if (it.adSize == null)
                it.setAdSize(
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context,
                        deviceCurrentWidth - padding * 2
                    )
                )
            try {
                it.adUnitId = adUnitId //Can only be set once
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (adListener != null)
                it.adListener = adListener

            it.loadAd(AdRequest.Builder().build())
        })

    }
}
@Composable
fun HomeContent(
    appState: AppState = rememberAppState(),
    ad:Int

    ) {

        Scaffold(
            bottomBar = {
              AnimatedVisibility(visible = appState.state.not(),
                  enter = slideInVertically(
                      initialOffsetY = {
                          it / 2
                      },
                  ),
                  exit = slideOutVertically(
                      targetOffsetY = {
                          it / 2
                      },
                  ),) {
                  NavigationBar() {

                      appState.topLevelDestination.forEach {
                          val selected =
                              appState.currentDestination.isTopLevelDestinationInHierarchy(
                                  it
                              )


                          NavigationBarItem(selected = selected,
                              onClick = {
                                  appState.navigateToLevelDestination(it)
                              },
                              icon = {
                                  val icon = if (selected) it.selectedIcon else it.unselectedIcon

                                  when (icon) {
                                      is Icon.ImageVectorIcon -> Icon(
                                          imageVector = icon.imageVector,
                                          contentDescription = null,
                                      )

                                      is Icon.DrawableResourceIcon -> Icon(
                                          painter = painterResource(id = icon.id),
                                          contentDescription = null,
                                      )
                                  }
                              },
                              label = { androidx.compose.material3.Text(text = it.titleTextId) }

                          )
                      }

                  }
              }
            }

        ) {
            Column {
              /*  if(ad==1){
                   AdmobBanner()
                }
                else if(ad==2){
                    FacebookBanner()
                }*/
                //AdmobBanner()
                AppNavHost(navController = appState.navHostController, modifier = Modifier
                    .padding(it)
                    .weight(1f)){
                }
            }
            }

        }




private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

