package org.signa.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.signa.app.domain.model.Signal
import org.signa.app.ui.theme.GraphColors
import org.signa.app.ui.utils.formatTimestamp


@Composable
fun SignalListItem(signal: Signal, onClick: () -> Unit) {
    val borderColor = when {
        signal.isSuspicious -> GraphColors.AlertRed
        signal.strength > -50 -> GraphColors.SignalGreen
        else -> GraphColors.StarlightWhite.copy(alpha = 0.1f)
    }

    val shadowAlpha = if (signal.isSuspicious || signal.strength > -50) 0.15f else 0f

    GlassyCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(modifier = Modifier.background(
            Brush.horizontalGradient(
                listOf(borderColor.copy(alpha = shadowAlpha), Color.Transparent)
            )
        )) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (signal.isSuspicious) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = GraphColors.AlertRed, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("SECURITY RISK", color = GraphColors.AlertRed, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    } else if (signal.strength > -50) {
                        Text("ACTIVE & STABLE", color = GraphColors.SignalGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }

                    Text(
                        text = signal.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${signal.type.name} • ${formatTimestamp(signal.timestamp)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GraphColors.CyberNeon.copy(alpha = 0.8f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${signal.strength} dBm",
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (signal.strength > -60) GraphColors.SignalGreen else Color.White,
                        fontWeight = FontWeight.Black
                    )
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(4.dp)
                            .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
                    ) {
                        val barWidth = ((signal.strength + 100) / 70f).coerceIn(0f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(barWidth)
                                .fillMaxHeight()
                                .background(borderColor, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }
}