package org.signa.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.signa.app.presentation.theme.GraphColors

@Composable
fun AppFooter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Created by Jamalzadeh",
            style = MaterialTheme.typography.labelMedium,
            color = GraphColors.StarlightWhite.copy(alpha = 0.7f)
        )
        Text(
            text = "Organization: Graph",
            style = MaterialTheme.typography.labelSmall,
            color = GraphColors.CyberNeon.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}
