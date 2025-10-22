package com.gwabs.getpayedpos.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import com.gwabs.getpayedpos.ui.screens.AddSaleScreen
import com.gwabs.getpayedpos.ui.screens.DashboardScreen
import com.gwabs.getpayedpos.ui.screens.EodScreen
import com.gwabs.getpayedpos.ui.screens.HistoryScreen

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.AccountCircle)
    data object AddSale   : Screen("add_sale",  "Add Sale",  Icons.Default.AddCircle)
    data object History   : Screen("history",   "History",   Icons.Default.Menu)
    data object Eod       : Screen("eod",       "EOD",       Icons.Default.DateRange)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNav() {
    val navController = rememberNavController()
    val items = listOf(Screen.Dashboard, Screen.AddSale, Screen.History, Screen.Eod)
    Scaffold(
        bottomBar = {
            NavigationBar {
                val current by navController.currentBackStackEntryAsState()
                val route = current?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = route == screen.route,
                        onClick = { if (route != screen.route) navController.navigate(screen.route) { launchSingleTop = true; popUpTo(navController.graph.startDestinationId){ saveState = true }; restoreState = true } },
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { inner ->
        NavHost(navController, startDestination = Screen.Dashboard.route, modifier = Modifier.padding(inner)) {
            composable(Screen.Dashboard.route) { DashboardScreen() }
            composable(Screen.AddSale.route)   { AddSaleScreen() }
            composable(Screen.History.route)   { HistoryScreen() }
            composable(Screen.Eod.route)       { EodScreen() }
        }
    }
}

