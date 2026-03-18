package com.yg.kick.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KickSessionDao {
    
    @Query("SELECT * FROM kick_sessions WHERE date = :date ORDER BY createdAt DESC")
    fun getSessionsByDate(date: Long): Flow<List<KickSession>>
    
    @Query("SELECT * FROM kick_sessions WHERE date = :date AND mealType = :mealType LIMIT 1")
    suspend fun getSessionByDateAndMeal(date: Long, mealType: String): KickSession?
    
    @Query("SELECT * FROM kick_sessions ORDER BY date DESC, createdAt DESC")
    fun getAllSessions(): Flow<List<KickSession>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: KickSession): Long
    
    @Update
    suspend fun updateSession(session: KickSession)
    
    @Query("DELETE FROM kick_sessions WHERE id = :id")
    suspend fun deleteSession(id: Long)
    
    @Query("DELETE FROM kick_sessions WHERE date = :date")
    suspend fun deleteSessionsByDate(date: Long)
    
    @Query("SELECT * FROM kick_sessions WHERE date = :date AND mealType = :mealType")
    fun observeSessionByDateAndMeal(date: Long, mealType: String): Flow<KickSession?>

    @Query("SELECT * FROM kick_sessions ORDER BY date DESC, createdAt DESC")
    suspend fun getAllSessionsSync(): List<KickSession>
}
