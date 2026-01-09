package org.signa.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SignalType { WIFI, BLUETOOTH, CELLULAR, OTHER }

@Serializable
data class SignalPoint(val timestamp: Long, val strength: Int)

@Serializable
data class Signal(
    val id: String,
    val type: SignalType,
    val name: String,
    val strength: Int,
    val macAddress: String? = null,
    val frequency: String? = null,
    val timestamp: Long,
    val firstSeen: Long = timestamp,
    val lastSeen: Long = timestamp,
    val isSuspicious: Boolean = false,
    val graphData: List<Float> = emptyList(),
    val history: List<SignalPoint> = emptyList(),
    val rawData: Map<String, String> = emptyMap()
)