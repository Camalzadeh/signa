package org.signa.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signals")
data class SignalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val strength: Int,
    val timestamp: Long,
    val isSuspicious: Boolean
)