package org.signa.app.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.signa.app.data.repository.SignalRepositoryImpl
import org.signa.app.data.service.MockAiService
import org.signa.app.data.source.MockSignalScanner
import org.signa.app.domain.model.Signal
import org.signa.app.presentation.components.AppFooter
import org.signa.app.presentation.components.GlassyCard
import org.signa.app.presentation.components.SignalRadar
import org.signa.app.presentation.theme.GraphColors
import org.signa.app.domain.util.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {

    val scanner = remember { MockSignalScanner() }
    val repository = remember { SignalRepositoryImpl(scanner) }
    val aiService = remember { MockAiService() }
    
    var signals by remember { mutableStateOf<List<Signal>>(emptyList()) }
    var analysisResult by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        repository.getSignals().collect { newSignals ->
            if (!isAnalyzing) {
                signals = newSignals
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GraphColors.DeepSpaceBlack, GraphColors.VoidBlack)
                )
            )
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar(
                title = { 
                    Text(
                        "SIGNA STATUS", 
                        style = MaterialTheme.typography.headlineMedium,
                        color = GraphColors.CyberNeon,
                        letterSpacing = 2.sp
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )


            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                if (maxWidth > 600.dp) {

                    Row(modifier = Modifier.fillMaxSize()) {

                        Box(
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxHeight()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            SignalRadar(modifier = Modifier.size(300.dp))
                            Text(
                                "${signals.size} TARGETS",
                                color = GraphColors.StarlightWhite.copy(alpha = 0.5f),
                                modifier = Modifier.padding(top = 350.dp)
                            )
                        }
                        

                        Column(modifier = Modifier.weight(0.6f).fillMaxHeight()) {
                            SignalGrid(signals, true)
                        }
                    }
                } else {

                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            SignalRadar(modifier = Modifier.size(200.dp))
                            Text(
                                "${signals.size} TARGETS",
                                color = GraphColors.StarlightWhite.copy(alpha = 0.5f),
                                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
                            )
                        }
                        SignalList(signals)
                    }
                }
            }
            
            AppFooter()
        }


        FloatingActionButton(
            onClick = {
                if (!isAnalyzing && signals.isNotEmpty()) {
                    isAnalyzing = true
                    scope.launch {
                        val result = aiService.analyzeSignals(signals)
                        isAnalyzing = false
                        if (result is Result.Success) {
                            analysisResult = result.data
                        } else {
                            analysisResult = "Analysis Failed"
                        }
                    }
                }
            },
            containerColor = GraphColors.CyberNeon,
            contentColor = GraphColors.VoidBlack,
            modifier = Modifier.align(Alignment.BottomEnd).padding(32.dp).padding(bottom = 64.dp)
        ) {
            if (isAnalyzing) {
                CircularProgressIndicator(color = GraphColors.VoidBlack, modifier = Modifier.size(24.dp))
            } else {
                Text("ANALYZE", fontWeight = FontWeight.Bold)
            }
        }
        

        if (analysisResult != null) {
            AlertDialog(
                onDismissRequest = { analysisResult = null },
                containerColor = GraphColors.DeepSpaceBlack.copy(alpha = 0.95f),
                title = { Text("GEMINI ANALYSIS", color = GraphColors.CyberNeon) },
                text = { Text(analysisResult ?: "", color = GraphColors.StarlightWhite) },
                confirmButton = {
                    Button(
                        onClick = { analysisResult = null },
                        colors = ButtonDefaults.buttonColors(containerColor = GraphColors.NebulaPurple)
                    ) {
                        Text("ACKNOWLEDGE")
                    }
                }
            )
        }
    }
}

@Composable
fun SignalList(signals: List<Signal>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(signals) { signal ->
            SignalItem(signal)
        }
    }
}

@Composable
fun SignalGrid(signals: List<Signal>, isDesktop: Boolean) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(signals) { signal ->
            SignalItem(signal)
        }
    }
}

@Composable
fun SignalItem(signal: Signal) {
    GlassyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = signal.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = GraphColors.StarlightWhite
                )
                Text(
                    text = signal.type.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = GraphColors.CyberNeon
                )
            }
            Text(
                text = "${signal.strength} dBm",
                style = MaterialTheme.typography.titleLarge,
                color = if (signal.strength > -60) GraphColors.SignalGreen else GraphColors.AlertRed.copy(alpha = 0.7f)
            )
        }
    }
}
