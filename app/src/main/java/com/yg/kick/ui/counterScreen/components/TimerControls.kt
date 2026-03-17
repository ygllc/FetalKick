package com.yg.kick.ui.counterScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yg.kick.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TimerControls(
    isTimerRunning: Boolean,
    isPaused: Boolean = false,
    onStartStopClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        Button(
            onClick = onStartStopClick,
            shapes = ButtonDefaults.shapes(shape = IconButtonDefaults.mediumRoundShape),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.weight(1f),
            contentPadding = if (isTimerRunning) PaddingValues(horizontal = 12.dp, vertical = 12.dp) else PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(
                if (isTimerRunning && !isPaused) Rounded.Pause else Rounded.Alarm,
                contentDescription = if (isTimerRunning) stringResource(R.string.stop_timer) else stringResource(R.string.start_timer)
            )
            Spacer(if (isTimerRunning) Modifier.width(4.dp) else Modifier.width(8.dp))
            Text(if (isTimerRunning && !isPaused) stringResource(R.string.stop_timer) else stringResource(R.string.start_timer))
        }
        if (isTimerRunning) {
            Button(
                onClick = onPauseClick,
                shapes = ButtonDefaults.shapes(shape = IconButtonDefaults.mediumRoundShape),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Icon(
                    if (isPaused) Rounded.PlayArrow else Rounded.Pause,
                    contentDescription = if (isPaused) stringResource(R.string.resume_timer) else stringResource(R.string.pause_timer)
                )
                Spacer(Modifier.width(8.dp))
                Text(if (isPaused) stringResource(R.string.resume_timer) else stringResource(R.string.pause_timer))
            }
        }
        Button(
            onClick = onResetClick,
            shapes = ButtonDefaults.shapes(shape = IconButtonDefaults.mediumRoundShape),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.weight(1f),
            contentPadding = if (isTimerRunning) PaddingValues(horizontal = 12.dp, vertical = 12.dp) else PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(Rounded.RestartAlt, contentDescription = stringResource(R.string.reset_timer), modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.width(8.dp))
            Text(text = stringResource(R.string.reset_timer))
        }
    }
}
