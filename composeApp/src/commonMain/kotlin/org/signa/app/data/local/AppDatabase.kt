package org.signa.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.signa.app.data.dao.AiDao
import org.signa.app.data.dao.SignalDao
import org.signa.app.data.entity.AiAnalysisEntity
import org.signa.app.data.entity.SignalEntity

@Database(
    entities = [SignalEntity::class, AiAnalysisEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun signalDao(): SignalDao
    abstract fun aiDao(): AiDao
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>