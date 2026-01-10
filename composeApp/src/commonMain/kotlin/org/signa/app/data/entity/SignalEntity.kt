package org.signa.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signals")
data class SignalEntity(
    @PrimaryKey val id: String,
    val type: String,
    val name: String,
    val strength: Int,
    val macAddress: String?,
    val frequency: String?,
    val timestamp: Long,
    val firstSeen: Long,
    val lastSeen: Long,
    val isSuspicious: Boolean,
    val graphDataJson: String,
    val historyJson: String,
    val rawDataJson: String
)