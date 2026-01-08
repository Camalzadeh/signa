package org.signa.app.data.service

import androidx.compose.runtime.Composable
import org.signa.app.domain.service.AiService

@Composable
expect fun getPlatformAiService(): AiService
