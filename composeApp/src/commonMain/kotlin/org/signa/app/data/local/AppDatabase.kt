package org.signa.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SignalEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun signalDao(): SignalDao
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>