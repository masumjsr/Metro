package com.masum.metro.ui.icon
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector

object KaziTvIcons {
    val Back = Icons.Rounded.ArrowBack
    val favorite = Icons.Filled.Favorite
    val favoriteBorder = Icons.Outlined.FavoriteBorder
    val home = Icons.Filled.Map
    val homeBorder = Icons.Outlined.Map
    val route =Icons.Default.Route
    val  routeBorder = Icons.Outlined.Route
    val about = Icons.Default.Info
    val aboutBorder = Icons.Outlined.Info
    val nearby = Icons.Default.NearMe
    val nearbyBorder = Icons.Outlined.NearMe



}
sealed class Icon{
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}

