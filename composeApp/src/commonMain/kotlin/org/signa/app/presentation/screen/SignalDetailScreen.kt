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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
        
        Text("SIGNAL HISTORY & STRENGTH", color = GraphColors.CyberNeon, style = MaterialTheme.typography.titleMedium)
        GlassyCard(modifier = Modifier.fillMaxWidth().height(250.dp)) {
            AdvancedSignalGraph(currentSignal.history)
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
fun AdvancedSignalGraph(history: List<org.signa.app.domain.model.SignalSample>) {
    // If no history, show placeholder
    if (history.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
             Text("No Data", color = GraphColors.StarlightWhite.copy(alpha = 0.5f))
        }
        return
    }

    // Interactive State
    var offsetX by remember { mutableStateOf(0f) }
    
    // Convert history to relative time (0 is latest)
    val sortedHistory = remember(history) { history.sortedBy { it.timestamp } }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            // Limit offset
            // internal logic: 1 pixel = ? time? Or just index?
            // Let's map pixels to indices.
            // density of points.
            
            val totalPoints = sortedHistory.size
            if (totalPoints < 2) return@Canvas
            
            val visibleCount = 100 // How many points to show at once
            val stepX = width / (visibleCount - 1)
            
            // max scroll: (totalPoints - visibleCount) * stepX
            // We want 0 offset to mean "showing the last visibleCount points".
            // Dragging right (positive dragAmount) should show older data (move window left).
            
            // Just clamp offset to reasonable bounds
            // let's interpret offsetX as "number of points shifted from end"
            
            val shiftIndex = (-offsetX / stepX).toInt().coerceIn(0, (totalPoints - visibleCount).coerceAtLeast(0))
            
            // Slice the history window
            val startIndex = (totalPoints - visibleCount - shiftIndex).coerceAtLeast(0)
            val endIndex = (startIndex + visibleCount).coerceAtMost(totalPoints)
            
            val visiblePoints = sortedHistory.subList(startIndex, endIndex)
            
            val maxStrength = -30f
            val minStrength = -100f
            
            val path = Path()
            
            visiblePoints.forEachIndexed { index, sample ->
                val normalizedY = ((sample.strength - minStrength) / (maxStrength - minStrength)).coerceIn(0f, 1f)
                val x = index * stepX
                val y = height * (1 - normalizedY)
                
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
                
                // Draw dots for interaction?
                drawCircle(
                    color = GraphColors.CyberNeon,
                    radius = 2.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(x, y)
                )
            }
            
            drawPath(
                path = path,
                color = GraphColors.CyberNeon,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
            
            // Grid
            drawLine(
                 color = GraphColors.StarlightWhite.copy(alpha = 0.1f),
                 start = androidx.compose.ui.geometry.Offset(0f, height * 0.5f),
                 end = androidx.compose.ui.geometry.Offset(width, height * 0.5f),
                 strokeWidth = 1.dp.toPx()
            )
        }
        
        // Overlay info
        Text(
            text = "History: ${sortedHistory.size} pts | Drag to scroll",
            color = GraphColors.StarlightWhite.copy(alpha = 0.5f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}
