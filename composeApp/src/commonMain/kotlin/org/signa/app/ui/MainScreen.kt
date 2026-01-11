package org.signa.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.core.parameter.parametersOf
import org.signa.app.ui.viewmodel.AiAnalysisViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.signa.app.ui.navigation.Route
import org.signa.app.ui.screen.HomeScreen
import org.signa.app.ui.screen.SettingsScreen
import org.signa.app.ui.screen.SignalDetailScreen
import org.signa.app.ui.screen.SignalListScreen
import org.signa.app.ui.theme.GraphColors
import org.signa.app.ui.viewmodel.SignalViewModel
import org.signa.app.ui.utils.NavigationItem
import org.signa.app.ui.viewmodel.AiAnalysesViewModel

@Composable
expect fun PermissionGate(onPermissionsGranted: @Composable () -> Unit)

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val signalViewModel: SignalViewModel = koinViewModel()
    val aiAnalysesViewModel: AiAnalysesViewModel = koinViewModel()

    val signals by signalViewModel.signals.collectAsState()

    PermissionGate {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = GraphColors.DeepSpaceBlack,
                    contentColor = GraphColors.CyberNeon
                ) {
                    val items = listOf(
                        NavigationItem("Home", Route.Home.route, Icons.Default.Home),
                        NavigationItem(
                            "Signals",
                            Route.Signals.route,
                            Icons.AutoMirrored.Filled.List
                        ),
                        NavigationItem("Settings", Route.Settings.route, Icons.Default.Settings)
                    )

                    items.forEach { item ->
                        val currentBackStack by navController.currentBackStackEntryAsState()
                        val currentRoute = currentBackStack?.destination?.route

                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(Route.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(GraphColors.DeepSpaceBlack, GraphColors.VoidBlack)
                        )
                    )
                    .padding(innerPadding)
            ) {
                NavHost(navController = navController, startDestination = Route.Home.route) {
                    composable(Route.Home.route) {
                        HomeScreen(signals)
                    }
                    composable(Route.Signals.route) {
                        SignalListScreen(signals) { signal ->
                            navController.navigate(Route.SignalDetail.createRoute(signal.id))
                        }
                    }
                    composable(Route.Settings.route) {
                        SettingsScreen(
                            onClearHistory = { signalViewModel.clearHistory() },
                            onClearAnalyses = { aiAnalysesViewModel.clearAnalyses() })
                    }
                    composable(Route.SignalDetail.route) { backStackEntry ->
                        val signalId = backStackEntry.arguments?.getString("signalId") ?: ""

                        val detailViewModel: AiAnalysisViewModel =
                            koinViewModel { parametersOf(signalId) }

                        SignalDetailScreen(detailViewModel) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}

