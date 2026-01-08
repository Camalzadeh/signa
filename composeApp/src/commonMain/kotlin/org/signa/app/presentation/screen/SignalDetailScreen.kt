package org.signa.app.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.signa.app.domain.model.Signal
import org.signa.app.presentation.theme.GraphColors
import org.signa.app.presentation.components.GlassyCard
import org.signa.app.data.service.MockAiService
import org.signa.app.domain.util.Result

@Composable
fun SignalDetailScreen(
    signal: Signal?,
    onBack: () -> Unit
) {
    if (signal == null) {
        Text("Signal not found", color = GraphColors.AlertRed)
        return
    }

    val aiService = remember { MockAiService() }
    var analysisResult by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = GraphColors.VoidBlack)) {
            Text("< Back", color = GraphColors.CyberNeon)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            signal.name,
            style = MaterialTheme.typography.headlineMedium,
            color = if (signal.isSuspicious) GraphColors.AlertRed else GraphColors.CyberNeon
        )
        
        Text("Type: ${signal.type}", color = GraphColors.StarlightWhite)
        Text("Strength: ${signal.strength} dBm", color = GraphColors.SignalGreen)
        Text("Timestamp: ${signal.timestamp}", color = GraphColors.StarlightWhite.copy(alpha = 0.5f))
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("WAVEFORM VISUALIZATION (FOURIER)", color = GraphColors.CyberNeon, style = MaterialTheme.typography.titleMedium)
        GlassyCard(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            SignalWaveformGraph(signal.graphData)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                if (!isAnalyzing) {
                    isAnalyzing = true
                    scope.launch {
                        // Analyze specific signal
                        analysisResult = "AI Analysis for ${signal.name}:\n" +
                                (aiService.analyzeSignals(listOf(signal)) as? Result.Success)?.data.orEmpty()
                        isAnalyzing = false
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = GraphColors.CyberNeon),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isAnalyzing) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = GraphColors.VoidBlack)
            } else {
                Text("ANALYZE SIGNAL", color = GraphColors.VoidBlack)
            }
        }
        
        if (analysisResult != null) {
            Spacer(modifier = Modifier.height(16.dp))
            GlassyCard(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(
                    analysisResult!!,
                    color = GraphColors.StarlightWhite,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun SignalWaveformGraph(data: List<Float>) {
    Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (data.isEmpty()) return@Canvas
        
        val width = size.width
        val height = size.height
        val stepX = width / (data.size - 1)
        
        val path = Path()
        path.moveTo(0f, height * (1 - data[0]))
        
        for (i in 1 until data.size) {
            path.lineTo(i * stepX, height * (1 - data[i]))
        }
        
        drawPath(
            path = path,
            color = GraphColors.CyberNeon,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
