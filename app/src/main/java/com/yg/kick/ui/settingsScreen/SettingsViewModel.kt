package com.yg.kick.ui.settingsScreen

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yg.kick.data.local.KickSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val targetKicks: Int = 10,
    val defaultSessionDuration: Int = 30,
    val darkModeEnabled: Boolean = false,
    val exportStatus: ExportStatus = ExportStatus.Idle
)

sealed class ExportStatus {
    data object Idle : ExportStatus()
    data object Loading : ExportStatus()
    data class Success(val uri: Uri, val displayPath: String, val fileName: String) : ExportStatus()
    data class Error(val message: String) : ExportStatus()
}

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
        prefs.edit { putBoolean("notifications_enabled", enabled) }
    }

    fun setTargetKicks(kicks: Int) {
        _uiState.update { it.copy(targetKicks = kicks) }
        prefs.edit { putInt("target_kicks", kicks) }
    }

    fun setDefaultSessionDuration(minutes: Int) {
        _uiState.update { it.copy(defaultSessionDuration = minutes) }
        prefs.edit { putInt("default_session_duration", minutes) }
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        _uiState.update { it.copy(darkModeEnabled = enabled) }
        prefs.edit { putBoolean("dark_mode_enabled", enabled) }
    }

    fun exportToCsv() {
        viewModelScope.launch {
            _uiState.update { it.copy(exportStatus = ExportStatus.Loading) }
            
            try {
                val repository = KickSessionRepository(getApplication())
                val sessions = repository.getAllSessionsSync()
                
                if (sessions.isEmpty()) {
                    _uiState.update { it.copy(exportStatus = ExportStatus.Error("No data to export")) }
                    return@launch
                }
                
                val filePath = withContext(Dispatchers.IO) {
                    exportSessionsToCsv(sessions)
                }
                
                _uiState.update {
                    it.copy(
                        exportStatus = ExportStatus.Success(
                            uri = filePath.uri,
                            displayPath = filePath.displayPath,
                            fileName = filePath.fileName
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(exportStatus = ExportStatus.Error(e.message ?: "Export failed")) }
            }
        }
    }

    private data class ExportResult(
        val uri: Uri,
        val displayPath: String,
        val fileName: String
    )

    private fun exportSessionsToCsv(sessions: List<com.yg.kick.data.local.KickSession>): ExportResult {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timestamp = System.currentTimeMillis()
        val fileName = "fetal_kick_export_$timestamp.csv"

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = getApplication<Application>().contentResolver
            val contentValues = android.content.ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: throw IllegalStateException("Unable to create export file")
            resolver.openOutputStream(uri)?.use { outputStream ->
                writeSessionsToCsv(outputStream, sessions, dateFormatter)
            } ?: throw IllegalStateException("Unable to open export file")

            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            ExportResult(
                uri = uri,
                displayPath = "${Environment.DIRECTORY_DOWNLOADS}/$fileName",
                fileName = fileName
            )
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            file.outputStream().use { outputStream ->
                writeSessionsToCsv(outputStream, sessions, dateFormatter)
            }
            val uri = FileProvider.getUriForFile(
                getApplication(),
                "${getApplication<Application>().packageName}.fileprovider",
                file
            )
            ExportResult(
                uri = uri,
                displayPath = file.absolutePath,
                fileName = fileName
            )
        }
    }

    private fun writeSessionsToCsv(
        outputStream: OutputStream,
        sessions: List<com.yg.kick.data.local.KickSession>,
        dateFormatter: DateTimeFormatter
    ) {
        OutputStreamWriter(outputStream).use { writer ->
            writer.append("ID,Date,Meal Type,Kick Count,Timer Seconds,Total Session Seconds,Target Kicks,Created At,Updated At\n")

            sessions.forEach { session ->
                val date = Instant.ofEpochMilli(session.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(dateFormatter)

                val createdAt = Instant.ofEpochMilli(session.createdAt)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                val updatedAt = Instant.ofEpochMilli(session.updatedAt)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                writer.append("${session.id},$date,${session.mealType},${session.kickCount},${session.timerSeconds},${session.totalSessionSeconds},${session.targetKicks},$createdAt,$updatedAt\n")
            }
        }
    }

    fun clearExportStatus() {
        _uiState.update { it.copy(exportStatus = ExportStatus.Idle) }
    }
}
