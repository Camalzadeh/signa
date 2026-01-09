package org.signa.app.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.signa.app.domain.model.Signal
import org.signa.app.presentation.theme.GraphColors
import org.signa.app.presentation.components.GlassyCard

@Composable
fun SignalListScreen(
    signals: List<Signal>,
    onSignalClick: (Signal) -> Unit
) {
    var filterType by remember { mutableStateOf<String?>(null) }
    var sortBy by remember { mutableStateOf("Priority") }

    val processedSignals = remember(signals, filterType, sortBy) {
        signals.filter { filterType == null || it.type.name == filterType }
            .sortedWith(
                compareByDescending<Signal> { it.isSuspicious }
                    .thenByDescending { it.strength > -50 }
                    .thenByDescending {
                        when (sortBy) {
                            "Strength" -> it.strength.toFloat()
                            else -> 0f
                        }
                    }
                    .thenBy { it.name }
            )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "SIGNAL RADAR",
                    style = MaterialTheme.typography.headlineMedium,
                    color = GraphColors.CyberNeon,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Monitoring ${signals.size} active nodes",
                    style = MaterialTheme.typography.labelSmall,
                    color = GraphColors.StarlightWhite.copy(alpha = 0.6f)
                )
            }

            val suspiciousCount = signals.count { it.isSuspicious }
            if (suspiciousCount > 0) {
                Surface(
                    color = GraphColors.AlertRed.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, GraphColors.AlertRed)
                ) {
                    Text(
                        "$suspiciousCount ALERTS",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = GraphColors.AlertRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("FILTERS & SORTING", color = GraphColors.StarlightWhite.copy(alpha = 0.4f), style = MaterialTheme.typography.labelSmall)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipItem(selected = filterType == null, label = "All", onClick = { filterType = null })
            FilterChipItem(selected = filterType == "WIFI", label = "WiFi", onClick = { filterType = "WIFI" })
            FilterChipItem(selected = filterType == "BLUETOOTH", label = "Bluetooth", onClick = { filterType = "BLUETOOTH" })

            VerticalDivider(modifier = Modifier.height(32.dp).padding(horizontal = 4.dp), color = Color.Gray.copy(alpha = 0.3f))

            FilterChipItem(selected = sortBy == "Strength", label = "Sort: Power", onClick = { sortBy = "Strength" })
            FilterChipItem(selected = sortBy == "Priority", label = "Sort: Priority", onClick = { sortBy = "Priority" })
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(processedSignals, key = { it.id }) { signal ->
                SignalListItem(signal, onClick = { onSignalClick(signal) })
            }
        }
    }
}

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
                        text = "${signal.type.name} • ${signal.timestamp}",
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

@Composable
fun FilterChipItem(selected: Boolean, label: String, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = GraphColors.CyberNeon,
            selectedLabelColor = GraphColors.VoidBlack,
            labelColor = GraphColors.StarlightWhite,
            containerColor = GraphColors.DeepSpaceBlack.copy(alpha = 0.5f)
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = GraphColors.CyberNeon.copy(alpha = 0.5f),
            selectedBorderColor = GraphColors.CyberNeon
        )
    )
}