package org.signa.app.domain.model

data class Signal(
    val id: String,
    val type: SignalType,
    val name: String,
    val strength: Int,
    val macAddress: String? = null,
    val frequency: String? = null,
    val timestamp: Long
)

enum class SignalType {
    WIFI,
    BLUETOOTH,
    CELLULAR,
    OTHER
}
