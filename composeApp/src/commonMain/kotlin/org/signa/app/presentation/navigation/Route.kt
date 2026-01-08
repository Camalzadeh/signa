package org.signa.app.presentation.navigation

sealed class Route(val route: String) {
    object Home : Route("home")
    object Signals : Route("signals")
    object Settings : Route("settings")
    object SignalDetail : Route("signal_detail/{signalId}") {
        fun createRoute(signalId: String) = "signal_detail/$signalId"
    }
}
