package org.signa.app.data.service

import androidx.compose.runtime.Composable
import org.signa.app.domain.service.AiService

@Composable
actual fun getPlatformAiService(): AiService {
    return MockAiService() // Fallback to mock on Desktop for now
}
