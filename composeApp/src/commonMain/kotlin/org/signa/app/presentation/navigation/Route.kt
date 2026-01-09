package org.signa.app.presentation.navigation

sealed class Route(val route: String) {
    data object Home : Route("home")
    data object Signals : Route("signals")
    data object Settings : Route("settings")
    data object SignalDetail : Route("signal_detail/{signalId}") {
        fun createRoute(signalId: String) = "signal_detail/$signalId"
    }
}
