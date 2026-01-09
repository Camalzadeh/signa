package org.signa.app.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.signa.app.data.repository.SignalRepositoryImpl
import org.signa.app.data.source.getPlatformScanner
import org.signa.app.domain.model.Signal
import org.signa.app.presentation.navigation.Route
import org.signa.app.presentation.screen.HomeScreen
import org.signa.app.presentation.screen.SettingsScreen
import org.signa.app.presentation.screen.SignalDetailScreen
import org.signa.app.presentation.screen.SignalListScreen
import org.signa.app.presentation.theme.GraphColors
import org.signa.app.data.local.getDatabaseBuilder

@Composable
expect fun PermissionGate(onPermissionsGranted: @Composable () -> Unit)

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val database = remember {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    val scanner = getPlatformScanner()
    val repository = remember(scanner, database) {
        SignalRepositoryImpl(scanner, database.signalDao())
    }

    // 2. Siqnalları bazadan real-time izləyirik
    val signals by repository.getSignals().collectAsState(initial = emptyList())

    PermissionGate {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = GraphColors.DeepSpaceBlack,
                    contentColor = GraphColors.CyberNeon
                ) {
                    val items = listOf(
                        NavigationItem("Home", Route.Home.route, Icons.Default.Home),
                        NavigationItem("Signals", Route.Signals.route,
                            Icons.AutoMirrored.Filled.List
                        ),
                        NavigationItem("Settings", Route.Settings.route, Icons.Default.Settings)
                    )

                    items.forEach { item ->
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
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
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = GraphColors.CyberNeon,
                                unselectedIconColor = GraphColors.StarlightWhite.copy(alpha = 0.5f),
                                indicatorColor = GraphColors.NebulaPurple.copy(alpha = 0.3f)
                            )
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
                    composable(Route.Home.route) { HomeScreen(signals) }
                    composable(Route.Signals.route) {
                        SignalListScreen(signals) { signal ->
                            navController.navigate(Route.SignalDetail.createRoute(signal.id))
                        }
                    }
                    composable(Route.Settings.route) {
                        // SettingsScreen-ə bazanı təmizləmək üçün DAO-nu ötürə bilərik
                        SettingsScreen()
                    }
                    composable(Route.SignalDetail.route) { backStackEntry ->
                        val signalId = backStackEntry.arguments?.getString("signalId")
                        SignalDetailScreen(signalId, repository) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}

data class NavigationItem(val label: String, val route: String, val icon: ImageVector)