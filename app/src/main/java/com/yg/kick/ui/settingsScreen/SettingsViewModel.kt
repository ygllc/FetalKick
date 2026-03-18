package com.yg.kick.ui.settingsScreen

import android.content.Context
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val targetKicks: Int = 10,
    val defaultSessionDuration: Int = 30,
    val darkModeEnabled: Boolean = false
)

class SettingsViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val prefs = application.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(loadSettings())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private fun loadSettings(): SettingsUiState {
        return SettingsUiState(
            notificationsEnabled = prefs.getBoolean("notifications_enabled", true),
            targetKicks = prefs.getInt("target_kicks", 10),
            defaultSessionDuration = prefs.getInt("default_session_duration", 30),
            darkModeEnabled = prefs.getBoolean("dark_mode_enabled", false)
        )
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    fun setTargetKicks(kicks: Int) {
        _uiState.update { it.copy(targetKicks = kicks) }
        prefs.edit().putInt("target_kicks", kicks).apply()
    }

    fun setDefaultSessionDuration(minutes: Int) {
        _uiState.update { it.copy(defaultSessionDuration = minutes) }
        prefs.edit().putInt("default_session_duration", minutes).apply()
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        _uiState.update { it.copy(darkModeEnabled = enabled) }
        prefs.edit().putBoolean("dark_mode_enabled", enabled).apply()
    }
}
