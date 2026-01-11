package org.signa.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.signa.app.ui.theme.GraphColors

@Composable
fun SignalRadar(
    modifier: Modifier = Modifier,
    isScanning: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition()
    

    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    

    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension / 2
        

        drawCircle(
            color = GraphColors.CyberNeon.copy(alpha = 0.1f),
            radius = maxRadius * 0.33f,
            style = Stroke(width = 1.dp.toPx())
        )
        drawCircle(
            color = GraphColors.CyberNeon.copy(alpha = 0.1f),
            radius = maxRadius * 0.66f,
            style = Stroke(width = 1.dp.toPx())
        )
        drawCircle(
            color = GraphColors.CyberNeon.copy(alpha = 0.1f),
            radius = maxRadius,
            style = Stroke(width = 1.dp.toPx())
        )


        if (isScanning) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GraphColors.CyberNeon.copy(alpha = 0f),
                        GraphColors.CyberNeon.copy(alpha = alpha)
                    ),
                    center = center,
                    radius = (maxRadius * scale).coerceAtLeast(0.1f)
                ),
                radius = maxRadius * scale,
                style = Stroke(width = 4.dp.toPx())
            )
        }
    }
}
