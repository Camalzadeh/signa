package org.signa.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SignalEntity::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun signalDao(): SignalDao
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>