package org.signa.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.signa.app.presentation.theme.GraphColors

@Composable
fun AppFooter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 12.dp),
            thickness = 0.5.dp,
            color = GraphColors.CyberNeon.copy(alpha = 0.3f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(GraphColors.SignalGreen, shape = androidx.compose.foundation.shape.CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "SIGNA SYSTEM ACTIVE",
                color = GraphColors.CyberNeon,
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}