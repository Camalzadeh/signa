package org.signa.app.presentation

import androidx.compose.runtime.Composable

@Composable
actual fun PermissionGate(onPermissionsGranted: @Composable () -> Unit) {
    onPermissionsGranted()
}