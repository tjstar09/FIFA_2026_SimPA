package com.fifa.simpa.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.Home as HomeIcon
import androidx.compose.material.icons.outlined.Home as HomeOutlined
import androidx.compose.material.icons.filled.Leaderboard as PointsTableIcon
import androidx.compose.material.icons.outlined.Leaderboard as PointsTableOutlined
import androidx.compose.material.icons.filled.Info as CreditsIcon
import androidx.compose.material.icons.outlined.Info as CreditsOutlined
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : Screen(
        route = "home",
        title = "Home",
        selectedIcon = HomeIcon,
        unselectedIcon = HomeOutlined
    )
    data object PointsTable : Screen(
        route = "pointstable",
        title = "Points Table",
        selectedIcon = PointsTableIcon,
        unselectedIcon = PointsTableOutlined
    )
    data object Credits : Screen(
        route = "credits",
        title = "Credits",
        selectedIcon = CreditsIcon,
        unselectedIcon = CreditsOutlined
    )
}

val bottomNavItems = listOf(Screen.Home, Screen.PointsTable, Screen.Credits)