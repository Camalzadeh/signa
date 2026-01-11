package org.signa.app.ui

import androidx.compose.runtime.Composable

@Composable
actual fun PermissionGate(onPermissionsGranted: @Composable () -> Unit) {
    onPermissionsGranted()
}