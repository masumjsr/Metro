package com.masum.metro.ui.navigation

import com.masum.metro.ui.icon.Icon
import com.masum.metro.ui.icon.KaziTvIcons

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val titleTextId: String,
){
    HOME(
        selectedIcon = Icon.ImageVectorIcon(KaziTvIcons.home),
        unselectedIcon = Icon.ImageVectorIcon(KaziTvIcons.homeBorder),
        titleTextId = "Map"
    ),

    Journey(
        selectedIcon = Icon.ImageVectorIcon(KaziTvIcons.route),
        unselectedIcon = Icon.ImageVectorIcon(KaziTvIcons.routeBorder),
        titleTextId = "Journey"
    ),

    Nearby(
        selectedIcon = Icon.ImageVectorIcon(KaziTvIcons.nearby),
        unselectedIcon = Icon.ImageVectorIcon(KaziTvIcons.nearbyBorder),
        titleTextId = "NearBy"
    ),
    About(
        selectedIcon = Icon.ImageVectorIcon(KaziTvIcons.about),
        unselectedIcon = Icon.ImageVectorIcon(KaziTvIcons.aboutBorder),
        titleTextId = "About"
    )
    ,


}