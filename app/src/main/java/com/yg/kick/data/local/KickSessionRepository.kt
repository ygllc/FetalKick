package com.yg.kick.data.local

import android.content.Context
import kotlinx.coroutines.flow.Flow

class KickSessionRepository(context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val dao = database.kickSessionDao()
    
    fun getSessionsByDate(date: Long): Flow<List<KickSession>> {
        return dao.getSessionsByDate(date)
    }
    
    fun getAllSessions(): Flow<List<KickSession>> {
        return dao.getAllSessions()
    }
    
    suspend fun getSessionByDateAndMeal(date: Long, mealType: String): KickSession? {
        return dao.getSessionByDateAndMeal(date, mealType)
    }
    
    fun observeSessionByDateAndMeal(date: Long, mealType: String): Flow<KickSession?> {
        return dao.observeSessionByDateAndMeal(date, mealType)
    }
    
    suspend fun saveSession(session: KickSession): Long {
        return dao.insertSession(session)
    }
    
    suspend fun updateSession(session: KickSession) {
        dao.updateSession(session.copy(updatedAt = System.currentTimeMillis()))
    }
    
    suspend fun saveOrUpdateSession(
        date: Long,
        mealType: String,
        kickCount: Int,
        timerSeconds: Int,
        totalSessionSeconds: Int,
        isTimerRunning: Boolean,
        targetKicks: Int = 10
    ) {
        val existing = dao.getSessionByDateAndMeal(date, mealType)
        if (existing != null) {
            dao.updateSession(
                existing.copy(
                    kickCount = kickCount,
                    timerSeconds = timerSeconds,
                    totalSessionSeconds = totalSessionSeconds,
                    isTimerRunning = isTimerRunning,
                    targetKicks = targetKicks,
                    updatedAt = System.currentTimeMillis()
                )
            )
        } else {
            dao.insertSession(
                KickSession(
                    date = date,
                    mealType = mealType,
                    kickCount = kickCount,
                    timerSeconds = timerSeconds,
                    totalSessionSeconds = totalSessionSeconds,
                    isTimerRunning = isTimerRunning,
                    targetKicks = targetKicks,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
    
    suspend fun deleteSession(id: Long) {
        dao.deleteSession(id)
    }
    
    suspend fun deleteSessionsByDate(date: Long) {
        dao.deleteSessionsByDate(date)
    }

    suspend fun getAllSessionsSync(): List<KickSession> {
        return dao.getAllSessionsSync()
    }
}
