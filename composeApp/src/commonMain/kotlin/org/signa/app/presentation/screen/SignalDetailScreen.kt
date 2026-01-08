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
import org.signa.app.data.service.getPlatformAiService
import org.signa.app.domain.util.Result

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import org.signa.app.domain.repository.SignalRepository

@Composable
fun SignalDetailScreen(
    signalId: String?,
    repository: SignalRepository,
    onBack: () -> Unit
) {
    var signal by remember { mutableStateOf<Signal?>(null) }
    
    LaunchedEffect(signalId) {
        if (signalId != null) {
            repository.getSignal(signalId).collect {
                signal = it
            }
        }
    }

    if (signal == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
             Text("Signal not found or lost...", color = GraphColors.AlertRed)
             Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
                 Text("Back")
             }
        }
        return
    }
    
    val currentSignal = signal!!

    val aiService = getPlatformAiService()
    var analysisResult by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState)) {
        Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = GraphColors.VoidBlack)) {
            Text("< Back", color = GraphColors.CyberNeon)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            currentSignal.name,
            style = MaterialTheme.typography.headlineMedium,
            color = if (currentSignal.isSuspicious) GraphColors.AlertRed else GraphColors.CyberNeon
        )
        
        Text("Type: ${currentSignal.type}", color = GraphColors.StarlightWhite)
        Text("Strength: ${currentSignal.strength} dBm", color = GraphColors.SignalGreen)
        Text("Timestamp: ${currentSignal.timestamp}", color = GraphColors.StarlightWhite.copy(alpha = 0.5f))
        
        if (currentSignal.rawData.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("RAW DATA", color = GraphColors.CyberNeon, style = MaterialTheme.typography.titleMedium)
            GlassyCard(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    currentSignal.rawData.forEach { (key, value) ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(key, color = GraphColors.StarlightWhite.copy(alpha = 0.7f))
                            Text(value, color = GraphColors.CyberNeon)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("WAVEFORM VISUALIZATION (FOURIER)", color = GraphColors.CyberNeon, style = MaterialTheme.typography.titleMedium)
        GlassyCard(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            SignalWaveformGraph(currentSignal.graphData)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                if (!isAnalyzing) {
                    isAnalyzing = true
                    scope.launch {
                        // Analyze specific signal
                        analysisResult = "AI Analysis for ${currentSignal.name}:\n" +
                                (aiService.analyzeSignals(listOf(currentSignal)) as? Result.Success)?.data.orEmpty()
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
