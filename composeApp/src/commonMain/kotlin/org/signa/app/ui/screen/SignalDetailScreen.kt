package org.signa.app.ui.screen

import AiResultView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.signa.app.ui.theme.GraphColors
import org.signa.app.ui.components.GlassyCard
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import org.signa.app.ui.components.SignalGraph
import org.signa.app.ui.utils.AiAnalysisState
import org.signa.app.ui.utils.formatTimestamp
import org.signa.app.ui.viewmodel.AiAnalysisViewModel

@Composable
fun SignalDetailScreen(
    viewModel: AiAnalysisViewModel,
    onBack: () -> Unit
) {
    val signal by viewModel.signal.collectAsState()
    val analysisState by viewModel.analysisState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = GraphColors.VoidBlack)
        ) {
            Text("< Back", color = GraphColors.CyberNeon)
        }

        Spacer(modifier = Modifier.height(24.dp))

        signal?.let { s ->
            Text(
                text = s.name,
                style = MaterialTheme.typography.headlineMedium,
                color = if (s.isSuspicious) GraphColors.AlertRed else GraphColors.CyberNeon
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Type: ${s.type}", color = GraphColors.StarlightWhite)
            Text("Strength: ${s.strength} dBm", color = GraphColors.SignalGreen)

            Spacer(modifier = Modifier.height(16.dp))

            Text("SIGNAL HISTORY", color = GraphColors.CyberNeon, style = MaterialTheme.typography.titleMedium)
            GlassyCard(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                SignalGraph(s.history)
            }
        } ?: run {
            Text("Loading signal details...", color = GraphColors.StarlightWhite)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "AI SECURITY ANALYSIS",
            color = GraphColors.CyberNeon,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (val s = analysisState) {
            is AiAnalysisState.Idle -> {
                GlassyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No AI analysis found for this signal.",
                            color = GraphColors.StarlightWhite,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.startNewAnalysis() },
                            colors = ButtonDefaults.buttonColors(containerColor = GraphColors.CyberNeon)
                        ) {
                            Text("Analyze with Gemini", color = GraphColors.VoidBlack)
                        }
                    }
                }
            }

            is AiAnalysisState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = GraphColors.CyberNeon)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Gemini is thinking...",
                            color = GraphColors.StarlightWhite.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            is AiAnalysisState.Success -> {
                val analysis = s.analysis

                Column {
                    GlassyCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            AiResultView(content = analysis.analysisText)

                            Spacer(modifier = Modifier.height(12.dp))

                            HorizontalDivider(color = GraphColors.StarlightWhite.copy(alpha = 0.1f))

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Last update: ${formatTimestamp(analysis.timestamp)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = GraphColors.StarlightWhite.copy(alpha = 0.4f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Yenidən analiz etmək imkanı
                    Button(
                        onClick = { viewModel.startNewAnalysis() },
                        colors = ButtonDefaults.buttonColors(containerColor = GraphColors.CyberNeon.copy(alpha = 0.8f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("REFRESH ANALYSIS", color = GraphColors.VoidBlack)
                    }
                }
            }

            is AiAnalysisState.Error -> {
                // Hər hansı xəta (internet və s.) baş verdikdə bura görünür
                GlassyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Analysis failed: ${s.message}",
                            color = GraphColors.AlertRed,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Button(
                            onClick = { viewModel.startNewAnalysis() },
                            modifier = Modifier.padding(top = 12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GraphColors.VoidBlack)
                        ) {
                            Text("Try Again", color = GraphColors.CyberNeon)
                        }
                    }
                }
            }
        }
    }
}