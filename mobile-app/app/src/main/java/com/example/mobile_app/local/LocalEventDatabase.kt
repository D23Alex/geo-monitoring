package com.example.mobile_app.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UnsentEvent::class, CachedSystemState::class], version = 1)
abstract class LocalEventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun systemStateDao(): SystemStateDao

    companion object {
        @Volatile
        private var INSTANCE: LocalEventDatabase? = null

        fun getInstance(context: Context): LocalEventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalEventDatabase::class.java,
                    "local_events.db"
                )
                    // При необходимости добавить миграции и/или шифрование
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
