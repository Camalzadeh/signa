package org.signa.app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.signa.app.presentation.theme.GraphColors

@Composable
fun GlassyCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    border: BorderStroke? = null, // Xüsusi border (məsələn, Alert üçün) ötürmək olar
    content: @Composable () -> Unit
) {
    // Şüşə effekti üçün daxili gradient (işıq yuxarı sol küncdən gəlir)
    val glassGradient = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.08f),
            Color.White.copy(alpha = 0.02f)
        )
    )

    // Standart kənar gradienti (əgər xüsusi border verilməyibsə)
    val defaultBorderBrush = Brush.linearGradient(
        colors = listOf(
            GraphColors.CyberNeon.copy(alpha = 0.4f),
            GraphColors.NebulaPurple.copy(alpha = 0.1f),
            GraphColors.CyberNeon.copy(alpha = 0.05f)
        )
    )

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(glassGradient)
            .then(
                if (border != null) Modifier.background(Color.Transparent)
                else Modifier.drawWithContent {
                    drawContent()
                    // İncə bir parıltı effekti (Reflective Edge)
                    drawRect(
                        brush = defaultBorderBrush,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                    )
                }
            ),
        color = GraphColors.DeepSpaceBlack.copy(alpha = 0.6f), // Şəffaflıq səviyyəsi
        border = border ?: BorderStroke(
            width = 1.dp,
            brush = defaultBorderBrush
        ),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Box(
            modifier = Modifier
                .padding(1.dp) // Borderin üzərinə çıxmaması üçün
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.03f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            content()
        }
    }
}