@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.yg.kick.ui.counterScreen.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yg.kick.ui.counterScreen.components.DateSelection
import com.yg.kick.ui.counterScreen.components.KickCounter
import com.yg.kick.ui.counterScreen.components.MealSelection
import com.yg.kick.ui.counterScreen.components.TimerControls
import com.yg.kick.ui.counterScreen.viewModels.KickCounterViewModel
import java.time.Instant
import java.time.ZoneId

@Composable
fun LunchScreen(
    viewModel: KickCounterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        viewModel.onDateSelected(date)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(initialHour = 0, initialMinute = 30, is24Hour = true)
        var isInputMode by remember { mutableStateOf(false) }

        Dialog(
            onDismissRequest = { showTimePicker = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            androidx.compose.material3.Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Set Session Duration",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    
                    if (isInputMode) {
                        TimeInput(state = timePickerState)
                    } else {
                        TimePicker(state = timePickerState)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
                    ) {
                        IconButton(
                            onClick = { isInputMode = !isInputMode },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = if (isInputMode) Icons.Default.Schedule else Icons.Default.Keyboard,
                                contentDescription = if (isInputMode) "Switch to picker" else "Switch to input"
                            )
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            val durationMinutes = timePickerState.hour * 60 + timePickerState.minute
                            viewModel.startTimer(durationMinutes)
                            showTimePicker = false
                        }) {
                            Text("Start")
                        }
                    }
                }
            }
        }
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
fun LunchScreenPreview() {
    LunchScreen()
}
