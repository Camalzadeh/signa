package org.signa.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.signa.app.ui.theme.GraphColors
import org.signa.app.ui.utils.formatTimestamp


@Composable
fun SignalGraph(history: List<org.signa.app.domain.model.SignalPoint>) {
    if (history.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Data", color = GraphColors.StarlightWhite.copy(alpha = 0.5f))
        }
        return
    }

    val sortedHistory = remember(history) { history.sortedBy { it.timestamp } }

    var scrollOffset by remember { mutableStateOf(0f) }
    val scale by remember { mutableStateOf(1f) }
    var selectedPointIndex by remember { mutableStateOf<Int?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(sortedHistory) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    scrollOffset += dragAmount.x
                }
            }
            .pointerInput(sortedHistory) {
                detectTapGestures { offset ->
                    val width = size.width.toFloat()
                    val pointSpacing = (width / 20f) * scale
                    val totalWidth = (sortedHistory.size - 1) * pointSpacing
                    val startX = (width - totalWidth) + scrollOffset

                    val index = ((offset.x - startX) / pointSpacing).toInt()
                    if (index in sortedHistory.indices) {
                        selectedPointIndex = index
                    }
                }
            }
    ) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        val pointSpacing = (width / 20f) * scale
        val maxStrength = -30f
        val minStrength = -100f

        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridStep = height / 4
            for (i in 0..4) {
                drawLine(
                    color = GraphColors.StarlightWhite.copy(alpha = 0.05f),
                    start = Offset(0f, i * gridStep),
                    end = Offset(width, i * gridStep),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val totalWidth = (sortedHistory.size - 1) * pointSpacing
            val startX = (width - totalWidth) + scrollOffset

            val path = Path()
            val pointOffsets = mutableListOf<Offset>()

            sortedHistory.forEachIndexed { index, sample ->
                val x = startX + (index * pointSpacing)
                val normalizedY = ((sample.strength - minStrength) / (maxStrength - minStrength)).coerceIn(0f, 1f)
                val y = height * (1 - normalizedY)

                val currentOffset = Offset(x, y)
                pointOffsets.add(currentOffset)

                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(
                path = path,
                color = GraphColors.CyberNeon,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )

            pointOffsets.forEachIndexed { index, offset ->
                if (offset.x in 0f..width) {
                    val isSelected = selectedPointIndex == index

                    drawCircle(
                        color = if (isSelected) GraphColors.SignalGreen else GraphColors.CyberNeon.copy(alpha = 0.5f),
                        radius = if (isSelected) 6.dp.toPx() else 2.dp.toPx(),
                        center = offset
                    )

                    if (isSelected) {
                        drawLine(
                            color = GraphColors.SignalGreen.copy(alpha = 0.3f),
                            start = Offset(offset.x, 0f),
                            end = Offset(offset.x, height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }
        }

        selectedPointIndex?.let { index ->
            val p = sortedHistory[index]
            Card(
                colors = CardDefaults.cardColors(containerColor = GraphColors.DeepSpaceBlack.copy(alpha = 0.8f)),
                modifier = Modifier.align(Alignment.TopCenter).padding(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GraphColors.CyberNeon)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Strength: ${p.strength} dBm", color = GraphColors.SignalGreen, style = MaterialTheme.typography.labelMedium)
                    Text("Time: ${formatTimestamp(p.timestamp)}", color = GraphColors.StarlightWhite, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Text(
            "DRAG TO EXPLORE | TAP TO INSPECT",
            color = GraphColors.CyberNeon.copy(alpha = 0.6f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.BottomStart).padding(4.dp)
        )
    }
}