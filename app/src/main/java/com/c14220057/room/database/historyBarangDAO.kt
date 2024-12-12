package com.c14220057.room.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.c14220057.room.historyBarang

@Dao
interface historyBarangDAO {
    @Insert
    suspend fun insert(history: historyBarang)

    @Query("SELECT * FROM historyBarang")
    suspend fun getAll(): List<historyBarang>

    @Delete
    suspend fun delete(history: historyBarang) // Fungsi untuk menghapus data
}

@Database(entities = [historyBarang::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyBarangDAO(): historyBarangDAO
}