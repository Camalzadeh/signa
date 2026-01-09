package org.signa.app.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.signa.app.domain.model.SignalPoint

class RoomConverters {
    @TypeConverter
    fun fromFloatList(value: List<Float>) = Json.encodeToString(value)
    @TypeConverter
    fun toFloatList(value: String): List<Float> = Json.decodeFromString(value)

    @TypeConverter
    fun fromHistory(value: List<SignalPoint>) = Json.encodeToString(value)
    @TypeConverter
    fun toHistory(value: String): List<SignalPoint> = Json.decodeFromString(value)

    @TypeConverter
    fun fromMap(value: Map<String, String>) = Json.encodeToString(value)
    @TypeConverter
    fun toMap(value: String): Map<String, String> = Json.decodeFromString(value)
}