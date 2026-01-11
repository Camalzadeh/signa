package org.signa.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.signa.app.ui.theme.GraphColors
import org.signa.app.ui.components.AppFooter

@Composable
expect fun PermissionStatusSection()

@Composable
fun SettingsScreen(
    onClearHistory: () -> Unit,
    onClearAnalyses: ()-> Unit
) {
    var showDialogForHistory by remember { mutableStateOf(false) }
    var showDialogForAnalyses by remember { mutableStateOf(false) }

    if (showDialogForHistory) {
        AlertDialog(
            onDismissRequest = { showDialogForHistory = false },
            title = { Text("Clear History") },
            text = { Text("Are you sure you want to delete all saved signals? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearHistory()
                        showDialogForHistory = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogForHistory = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    if(showDialogForAnalyses) {
        AlertDialog(
            onDismissRequest = { showDialogForAnalyses = false },
            title = { Text("Clear AI Analyses") },
            text = { Text("Are you sure you want to delete all AI analyses? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearAnalyses()
                        showDialogForAnalyses = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogForAnalyses = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "SETTINGS",
            style = MaterialTheme.typography.headlineMedium,
            color = GraphColors.CyberNeon
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "PERMISSIONS STATUS",
            color = GraphColors.CyberNeon,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        PermissionStatusSection()

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "DATA MANAGEMENT",
            color = GraphColors.CyberNeon,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialogForHistory = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Clear All Saved Signals")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showDialogForAnalyses = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Clear All AI Analyses")
        }

        Spacer(modifier = Modifier.weight(1f))
        AppFooter()
    }
}