package org.signa.app.data.mapper

import org.signa.app.data.entity.SignalEntity as SignalEntity
import org.signa.app.domain.model.Signal as SignalModel
import org.signa.app.domain.model.SignalType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun SignalModel.toEntity(): SignalEntity = SignalEntity(
    id = id,
    type = type.name,
    name = name,
    strength = strength,
    macAddress = macAddress,
    frequency = frequency,
    timestamp = timestamp,
    firstSeen = firstSeen,
    lastSeen = lastSeen,
    isSuspicious = isSuspicious,
    graphDataJson = Json.encodeToString(graphData),
    historyJson = Json.encodeToString(history),
    rawDataJson = Json.encodeToString(rawData)
)

fun SignalEntity.toDomain(): SignalModel = SignalModel(
    id = id,
    type = SignalType.valueOf(type),
    name = name,
    strength = strength,
    macAddress = macAddress,
    frequency = frequency,
    timestamp = timestamp,
    firstSeen = firstSeen,
    lastSeen = lastSeen,
    isSuspicious = isSuspicious,
    graphData = Json.decodeFromString(graphDataJson),
    history = Json.decodeFromString(historyJson),
    rawData = Json.decodeFromString(rawDataJson)
)