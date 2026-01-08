package org.signa.app.domain.model

data class Signal(
    val id: String,
    val type: SignalType,
    val name: String,
    val strength: Int,
    val macAddress: String? = null,
    val frequency: String? = null,
    val timestamp: Long,
    val isSuspicious: Boolean = false,
    val graphData: List<Float> = emptyList(), // Normalized 0f..1f for graphing
    val rawData: Map<String, String> = emptyMap()
)

enum class SignalType {
    WIFI,
    BLUETOOTH,
    CELLULAR,
    OTHER
}
