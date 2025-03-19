package com.example.mobile_app.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SystemStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: CachedSystemState)

    @Query("SELECT * FROM system_state ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestState(): CachedSystemState?
}
