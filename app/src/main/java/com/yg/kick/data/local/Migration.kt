package com.yg.kick.data.local

import androidx.room.migration.Migration

val MIGRATION_1_2 = Migration(1, 2) { db ->
    db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_kick_sessions_date_mealType ON kick_sessions(date, mealType)")
}

