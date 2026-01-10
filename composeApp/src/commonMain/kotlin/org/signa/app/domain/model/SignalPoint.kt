package org.signa.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SignalPoint(val timestamp: Long, val strength: Int)