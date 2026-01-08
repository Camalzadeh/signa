package org.signa.app.domain.model

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
    val graphData: List<Float> = emptyList(), // Keep for simple preview
    val history: List<SignalSample> = emptyList(), // True history
    val rawData: Map<String, String> = emptyMap()
)

data class SignalSample(
    val timestamp: Long,
    val strength: Int
)

enum class SignalType {
    WIFI,
    BLUETOOTH,
    CELLULAR,
    OTHER
}
