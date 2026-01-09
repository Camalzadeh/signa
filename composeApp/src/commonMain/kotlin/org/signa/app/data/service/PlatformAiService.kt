package org.signa.app.data.service

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.signa.app.domain.service.AiService
import org.signa.app.BuildConfig

@Composable
fun getAiService(model: String = "gemini-2.5-flash"): AiService {
    return remember(model) {
        GeminiAiService(
            apiKey = BuildConfig.GEMINI_API_KEY,
            modelName = model
        )
    }
}