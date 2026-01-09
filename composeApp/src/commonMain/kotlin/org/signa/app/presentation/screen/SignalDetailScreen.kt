package org.signa.app.presentation.screen

import AiResultView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp



import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.launch
import org.signa.app.domain.model.Signal
import org.signa.app.presentation.theme.GraphColors
import org.signa.app.presentation.components.GlassyCard
import org.signa.app.data.service.getAiService
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
             Text("org.signa.app.domain.model.Signal not found or lost...", color = GraphColors.AlertRed)
             Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
                 Text("Back")
             }
        }
        return
    }
    
    val currentSignal = signal!!

    val aiService = getAiService()
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
                AiResultView(
                    content = analysisResult!!,
                )
            }
        }
    }
}

@Composable
fun AdvancedSignalGraph(history: List<org.signa.app.domain.model.SignalPoint>) {
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
                    // Toxunulan nöqtəyə ən yaxın indeksi tapmaq
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

            // Nöqtələri və seçilmiş nöqtəni çək
            pointOffsets.forEachIndexed { index, offset ->
                if (offset.x in 0f..width) { // Yalnız ekranda olanları çək
                    val isSelected = selectedPointIndex == index

                    drawCircle(
                        color = if (isSelected) GraphColors.SignalGreen else GraphColors.CyberNeon.copy(alpha = 0.5f),
                        radius = if (isSelected) 6.dp.toPx() else 2.dp.toPx(),
                        center = offset
                    )

                    if (isSelected) {
                        // Seçilmiş nöqtə üçün şaquli xətt
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
                    Text("Time: ${p.timestamp}", color = GraphColors.StarlightWhite, style = MaterialTheme.typography.labelSmall)
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