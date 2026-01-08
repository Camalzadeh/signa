package org.signa.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.map
import org.signa.app.data.repository.SignalRepositoryImpl
import org.signa.app.data.source.MockSignalScanner
import org.signa.app.domain.model.Signal
import org.signa.app.domain.repository.SignalRepository

@Composable
fun App() {
    MaterialTheme {
        // Simple DI for now
        val scanner = remember { MockSignalScanner() }
        val repository = remember { SignalRepositoryImpl(scanner) }
        val aiService = remember { org.signa.app.data.service.MockAiService() }
        
        var signals by remember { mutableStateOf<List<Signal>>(emptyList()) }
        var analysisResult by remember { mutableStateOf<String?>(null) }
        var isAnalyzing by remember { mutableStateOf(false) }
        
        // Scope for coroutines triggered by UI events
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            repository.getSignals().collect { newSignals ->
                // Only update signals if not analyzing to keep view stable-ish or valid for analysis
                if (!isAnalyzing) {
                    signals = newSignals
                }
            }
        }

        Scaffold(
            topBar = {
                SmallTopAppBar(title = { Text("Signa Monitor") })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (!isAnalyzing && signals.isNotEmpty()) {
                            isAnalyzing = true
                            kotlinx.coroutines.launch(kotlinx.coroutines.Dispatchers.Default) {
                                // In real app, use ViewModelScope
                                scope.launch {
                                    val result = aiService.analyzeSignals(signals)
                                    isAnalyzing = false
                                    if (result is org.signa.app.domain.util.Result.Success) {
                                        analysisResult = result.data
                                    } else {
                                        analysisResult = "Analysis failed."
                                    }
                                }
                            }
                        }
                    }
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text("Analyze")
                    }
                }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    if (signals.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                            Text("Scanning for signals...", modifier = Modifier.padding(top = 64.dp))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(signals) { signal ->
                                SignalCard(signal)
                            }
                        }
                    }
                }
                
                if (analysisResult != null) {
                    AlertDialog(
                        onDismissRequest = { analysisResult = null },
                        title = { Text("Gemini Analysis") },
                        text = { Text(analysisResult ?: "") },
                        confirmButton = {
                            TextButton(onClick = { analysisResult = null }) {
                                Text("Close")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SignalCard(signal: Signal) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = signal.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${signal.strength} dBm",
                    color = if (signal.strength > -60) Color.Green else Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Type: ${signal.type}", style = MaterialTheme.typography.bodySmall)
            Text(text = "ID: ${signal.id}", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun SmallTopAppBar(title: @Composable () -> Unit) {
    // Basic implementation since experimental M3 APIs can be tricky with imports
    Surface(shadowElevation = 2.dp, color = MaterialTheme.colorScheme.primaryContainer) {
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
                title()
            }
        }
    }
}
