@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.yg.kick.ui.counterScreen.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yg.kick.ui.counterScreen.components.DateSelection
import com.yg.kick.ui.counterScreen.components.DatePickerModal
import com.yg.kick.ui.counterScreen.components.KickCounter
import com.yg.kick.ui.counterScreen.components.MealSelection
import com.yg.kick.ui.counterScreen.components.SessionDurationDialog
import com.yg.kick.ui.counterScreen.components.TimerControls
import com.yg.kick.ui.counterScreen.viewModels.KickCounterViewModel

@Composable
fun BreakfastScreen(
    viewModel: KickCounterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerModal(
            initialDate = uiState.selectedDate,
            onDateSelected = viewModel::onDateSelected,
            onDismiss = { showDatePicker = false }
        )
    }

    if (showTimePicker) {
        SessionDurationDialog(
            onDurationSelected = viewModel::startTimer,
            onDismiss = { showTimePicker = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        MealSelection(
            selectedMeal = uiState.selectedMeal,
            onMealSelected = viewModel::onMealSelected
        )
        Spacer(modifier = Modifier.height(24.dp))
        DateSelection(
            selectedDate = uiState.selectedDate,
            onDateClick = { showDatePicker = true },
            onSecondaryClick = { viewModel.decrementKickCount() }
        )
        Spacer(modifier = Modifier.height(32.dp))
        KickCounter(
            kickCount = uiState.currentKickCount,
            timerText = uiState.timerText,
            onKickIncrement = viewModel::incrementKickCount
        )
        Spacer(modifier = Modifier.height(32.dp))
        LinearWavyProgressIndicator(
            progress = { uiState.progress },
            modifier = Modifier.fillMaxWidth(0.8f).height(10.dp),
            color = MaterialTheme.colorScheme.tertiary,
            trackColor = Color.LightGray.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        TimerControls(
            isTimerRunning = uiState.isTimerRunning,
            isPaused = uiState.isTimerPaused,
            onStartStopClick = {
                if (uiState.isTimerRunning) viewModel.stopTimer() else showTimePicker = true
            },
            onPauseClick = {
                if (uiState.isTimerPaused) viewModel.resumeTimer() else viewModel.pauseTimer()
            },
            onResetClick = viewModel::resetTimer
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun BreakfastScreenPreview() {
    BreakfastScreen()
}
