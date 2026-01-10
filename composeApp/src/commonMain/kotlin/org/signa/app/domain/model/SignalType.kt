package org.signa.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SignalType { WIFI, BLUETOOTH, CELLULAR, OTHER }

