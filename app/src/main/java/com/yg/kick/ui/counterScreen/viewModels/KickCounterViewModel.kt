package com.yg.kick.ui.counterScreen.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yg.kick.data.local.KickSessionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class KickCounterViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KickCounterViewModel::class.java)) {
            return KickCounterViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

enum class MealType {
    BREAKFAST, LUNCH, SNACKS, DINNER
}

enum class Screen {
    COUNTER, HISTORY
}

data class MealSessionData(
    val kickCount: Int = 0,
    val timerSeconds: Int = 0,
    val totalSessionSeconds: Int = 0,
    val isTimerRunning: Boolean = false,
    val isPaused: Boolean = false
)

data class KickCounterUiState(
    val selectedMeal: MealType = MealType.BREAKFAST,
    val selectedDate: LocalDate = LocalDate.now(),
    val mealData: Map<MealType, MealSessionData> = MealType.entries.associateWith { MealSessionData() },
    val targetKicks: Int = 10,
    val currentScreen: Screen = Screen.COUNTER
) {
    val currentMealData: MealSessionData
        get() = mealData[selectedMeal] ?: MealSessionData()

    val progress: Float
        get() {
            val data = currentMealData
            return if (data.totalSessionSeconds > 0) {
                (data.timerSeconds.toFloat() / data.totalSessionSeconds.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
        }

    val timerText: String
        get() {
            val seconds = currentMealData.timerSeconds
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return "%02d:%02d".format(minutes, remainingSeconds)
        }
    
    val currentKickCount: Int
        get() = currentMealData.kickCount
    
    val isTimerPaused: Boolean
        get() = currentMealData.isPaused
    
    val isTimerRunning: Boolean
        get() = currentMealData.isTimerRunning
}

class KickCounterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KickSessionRepository(application)
    
    private val _uiState = MutableStateFlow(KickCounterUiState())
    val uiState: StateFlow<KickCounterUiState> = _uiState.asStateFlow()

    private val timerJobs = mutableMapOf<MealType, Job?>()
    
    private var isLoading = false

    init {
        loadSessionForCurrentMealAndDate()
    }

    private fun loadSessionForCurrentMealAndDate() {
        if (isLoading) return
        isLoading = true
        
        val dateMillis = _uiState.value.selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        val mealType = _uiState.value.selectedMeal.name
        
        viewModelScope.launch {
            val session = repository.getSessionByDateAndMeal(dateMillis, mealType)
            if (session != null) {
                _uiState.update { state ->
                    val updatedMealData = state.mealData.toMutableMap()
                    updatedMealData[state.selectedMeal] = MealSessionData(
                        kickCount = session.kickCount,
                        timerSeconds = session.timerSeconds,
                        totalSessionSeconds = session.totalSessionSeconds,
                        isTimerRunning = false
                    )
                    state.copy(
                        mealData = updatedMealData,
                        targetKicks = session.targetKicks
                    )
                }
            }
            isLoading = false
        }
    }

    private fun saveCurrentSession() {
        val state = _uiState.value
        val currentData = state.mealData[state.selectedMeal] ?: return
        
        val dateMillis = state.selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        
        viewModelScope.launch {
            repository.saveOrUpdateSession(
                date = dateMillis,
                mealType = state.selectedMeal.name,
                kickCount = currentData.kickCount,
                timerSeconds = currentData.timerSeconds,
                totalSessionSeconds = currentData.totalSessionSeconds,
                isTimerRunning = currentData.isTimerRunning,
                targetKicks = state.targetKicks
            )
        }
    }

    fun onMealSelected(index: Int) {
        saveCurrentSession()
        val meal = MealType.entries[index]
        _uiState.update { it.copy(selectedMeal = meal) }
        loadSessionForCurrentMealAndDate()
    }

    fun navigateToHistory() {
        _uiState.update { it.copy(currentScreen = Screen.HISTORY) }
    }

    fun navigateToCounter() {
        _uiState.update { it.copy(currentScreen = Screen.COUNTER) }
    }

    fun onDateSelected(date: LocalDate) {
        saveCurrentSession()
        _uiState.update { it.copy(selectedDate = date) }
        loadSessionForCurrentMealAndDate()
    }

    fun incrementKickCount() {
        val meal = _uiState.value.selectedMeal
        _uiState.update { state ->
            val updatedMealData = state.mealData.toMutableMap()
            val currentData = updatedMealData[meal] ?: MealSessionData()
            updatedMealData[meal] = currentData.copy(kickCount = currentData.kickCount + 1)
            state.copy(mealData = updatedMealData)
        }
        saveCurrentSession()
    }

    fun decrementKickCount() {
        val meal = _uiState.value.selectedMeal
        _uiState.update { state ->
            val updatedMealData = state.mealData.toMutableMap()
            val currentData = updatedMealData[meal] ?: MealSessionData()
            if (currentData.kickCount > 0) {
                updatedMealData[meal] = currentData.copy(kickCount = currentData.kickCount - 1)
                state.copy(mealData = updatedMealData)
            } else {
                state
            }
        }
        saveCurrentSession()
    }

    fun startTimer(durationMinutes: Int) {
        val meal = _uiState.value.selectedMeal
        if (_uiState.value.mealData[meal]?.isTimerRunning == true) return
        
        val durationSeconds = durationMinutes * 60
        _uiState.update { state ->
            val updatedMealData = state.mealData.toMutableMap()
            updatedMealData[meal] = MealSessionData(
                isTimerRunning = true,
                isPaused = false,
                totalSessionSeconds = durationSeconds,
                timerSeconds = 0,
                kickCount = state.mealData[meal]?.kickCount ?: 0
            )
            state.copy(mealData = updatedMealData)
        }
        
        saveCurrentSession()
        
        timerJobs[meal]?.cancel()
        timerJobs[meal] = viewModelScope.launch {
            while (true) {
                val currentData = _uiState.value.mealData[meal] ?: break
                if (currentData.timerSeconds >= currentData.totalSessionSeconds) break
                
                if (!currentData.isPaused) {
                    delay(1000)
                    _uiState.update { state ->
                        val innerUpdatedMealData = state.mealData.toMutableMap()
                        val data = innerUpdatedMealData[meal] ?: return@update state
                        innerUpdatedMealData[meal] = data.copy(timerSeconds = data.timerSeconds + 1)
                        state.copy(mealData = innerUpdatedMealData)
                    }
                } else {
                    delay(100)
                }
            }
            stopTimer(meal)
        }
    }

    fun pauseTimer(meal: MealType = _uiState.value.selectedMeal) {
        _uiState.update { state ->
            val updatedMealData = state.mealData.toMutableMap()
            val data = updatedMealData[meal] ?: return@update state
            updatedMealData[meal] = data.copy(isPaused = true)
            state.copy(mealData = updatedMealData)
        }
        saveCurrentSession()
    }

    fun resumeTimer(meal: MealType = _uiState.value.selectedMeal) {
        _uiState.update { state ->
            val updatedMealData = state.mealData.toMutableMap()
            val data = updatedMealData[meal] ?: return@update state
            updatedMealData[meal] = data.copy(isPaused = false)
            state.copy(mealData = updatedMealData)
        }
        saveCurrentSession()
    }

    fun stopTimer(meal: MealType = _uiState.value.selectedMeal) {
        timerJobs[meal]?.cancel()
        _uiState.update { state ->
            val updatedMealData = state.mealData.toMutableMap()
            val data = updatedMealData[meal] ?: return@update state
            updatedMealData[meal] = data.copy(isTimerRunning = false)
            state.copy(mealData = updatedMealData)
        }
        saveCurrentSession()
    }

    fun resetTimer() {
        val meal = _uiState.value.selectedMeal
        val currentKickCount = _uiState.value.mealData[meal]?.kickCount ?: 0
        stopTimer(meal)
        _uiState.update { state ->
            val updatedMealData = state.mealData.toMutableMap()
            updatedMealData[meal] = MealSessionData(kickCount = currentKickCount)
            state.copy(mealData = updatedMealData)
        }
        saveCurrentSession()
    }

    override fun onCleared() {
        super.onCleared()
        timerJobs.values.forEach { it?.cancel() }
        saveCurrentSession()
    }
}
