package org.signa.app.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.signa.app.data.repository.SignalRepositoryImpl
import org.signa.app.data.service.getPlatformAiService
import org.signa.app.data.source.getPlatformScanner
import org.signa.app.domain.model.Signal
import org.signa.app.presentation.navigation.Route
import org.signa.app.presentation.screen.HomeScreen
import org.signa.app.presentation.screen.SettingsScreen
import org.signa.app.presentation.screen.SignalDetailScreen
import org.signa.app.presentation.screen.SignalListScreen
import org.signa.app.presentation.theme.GraphColors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    // Dependencies
    val scanner = getPlatformScanner()
    val repository = remember(scanner) { SignalRepositoryImpl(scanner) }
    
    var signals by remember { mutableStateOf<List<Signal>>(emptyList()) }
    
    // Permissions Handling
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.CHANGE_WIFI_STATE,
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            // Add Bluetooth Scan/Connect if targeting Android 12+
             android.Manifest.permission.BLUETOOTH_SCAN,
             android.Manifest.permission.BLUETOOTH_CONNECT
        )
    )

    LaunchedEffect(Unit) {
        if (!multiplePermissionsState.allPermissionsGranted) {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
        repository.getSignals().collect { newSignals ->
            signals = newSignals
        }
    }

    if (!multiplePermissionsState.allPermissionsGranted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Text("Permissions Required", style = MaterialTheme.typography.headlineMedium, color = GraphColors.AlertRed)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Signa requires Location, WiFi, and Bluetooth permissions to scan for signals.", color = GraphColors.StarlightWhite)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }, colors = ButtonDefaults.buttonColors(containerColor = GraphColors.CyberNeon)) {
                     Text("Grant Permissions", color = GraphColors.VoidBlack)
                }
            }
        }
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = GraphColors.DeepSpaceBlack,
                    contentColor = GraphColors.CyberNeon
                ) {
                    val items = listOf(
                        NavigationItem("Home", Route.Home.route, Icons.Default.Home),
                        NavigationItem("Signals", Route.Signals.route, Icons.Default.List),
                        NavigationItem("Settings", Route.Settings.route, Icons.Default.Settings)
                    )
                    
                    // Track current route for selection state (omitted simple version)
                    
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = false, 
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(Route.Home.route)
                                    launchSingleTop = true
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
                    composable(Route.Home.route) {
                        HomeScreen(signals)
                    }
                    composable(Route.Signals.route) {
                        SignalListScreen(signals) { signal ->
                            navController.navigate(Route.SignalDetail.createRoute(signal.id))
                        }
                    }
                    composable(Route.Settings.route) {
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
