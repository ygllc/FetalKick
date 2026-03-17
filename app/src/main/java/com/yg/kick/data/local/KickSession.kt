package com.yg.kick.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "kick_sessions",
    indices = [Index(value = ["date", "mealType"], unique = true)]
)
data class KickSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long,
    val mealType: String,
    val kickCount: Int,
    val timerSeconds: Int,
    val totalSessionSeconds: Int,
    val isTimerRunning: Boolean,
    val targetKicks: Int = 10,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
