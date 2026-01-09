package org.signa.app.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val userHome = System.getProperty("user.home")
    val appDir = File(userHome, ".signa_app")

    if (!appDir.exists()) {
        appDir.mkdirs()
    }

    val dbFile = File(appDir, "signa_signals.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
}