package com.yg.kick.ui.historyScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yg.kick.R
import com.yg.kick.data.local.KickSession
import com.yg.kick.ui.theme.MyMileageShapeDefaults.bottomListItemShape
import com.yg.kick.ui.theme.MyMileageShapeDefaults.cardShape
import com.yg.kick.ui.theme.MyMileageShapeDefaults.middleListItemShape
import com.yg.kick.ui.theme.MyMileageShapeDefaults.topListItemShape
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Composable that represents the History screen, responsible for displaying a list of recorded kick sessions.
 *
 * This screen serves as a stateful wrapper around [HistoryScreenContent], fetching session data
 * from the [HistoryViewModel].
 *
 * @param viewModel The ViewModel that provides the session data and handles business logic.
 * @param onNavigateBack Callback function to be executed when the user requests to navigate back.
 */
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel(), onNavigateBack: () -> Unit = {}
) {
    val sessions by viewModel.sessions.collectAsState()
    HistoryScreenContent(
        sessions = sessions, 
        onNavigateBack = onNavigateBack,
        onDeleteSession = viewModel::deleteSession
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(
    sessions: List<KickSession>, 
    onNavigateBack: () -> Unit = {},
    onDeleteSession: (Long) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.history),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (sessions.isEmpty()) {
            EmptyHistoryState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                itemsIndexed(
                    items = sessions, key = { _, session -> session.id }) { index, session ->
                    HistoryCard(
                        session = session,
                        isFirst = index == 0,
                        isLast = index == sessions.lastIndex,
                        onDelete = { onDeleteSession(session.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyHistoryState() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.History,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_history),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
            )
            Text(
                text = stringResource(R.string.start_tracking_kicks),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryCard(
    session: KickSession,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    isMiddle: Boolean = false,
    onDelete: () -> Unit = {}
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    val date = Instant.ofEpochMilli(session.date).atZone(ZoneId.systemDefault()).toLocalDate()

    val mealTypeDisplay = when (session.mealType) {
        stringResource(R.string.breakfast_for_when) -> stringResource(R.string.breakfast)
        stringResource(R.string.lunch_for_when) -> stringResource(R.string.lunch)
        stringResource(R.string.snacks_for_when) -> stringResource(R.string.snacks)
        stringResource(R.string.dinner_for_when) -> stringResource(R.string.dinner)
        else -> session.mealType
    }

    val cardShape = when {
        isFirst -> topListItemShape()
        isLast -> bottomListItemShape()
        isMiddle -> middleListItemShape()
        else -> cardShape
    }

    Card(
        modifier = Modifier.fillMaxWidth(), shape = cardShape, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = date.format(/* formatter = */ dateFormatter),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mealTypeDisplay,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                if (session.timerSeconds > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val minutes = session.timerSeconds / 60
                        val seconds = session.timerSeconds % 60
                        Text(
                            text = stringResource(R.string._02d_02d).format(minutes, seconds),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.kick_counts, session.kickCount),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = stringResource(R.string.kicks),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryPreview() {
    val sampleSessions = listOf(
        KickSession(
            id = 1,
            date = System.currentTimeMillis(),
            mealType = "BREAKFAST",
            kickCount = 10,
            timerSeconds = 1800,
            totalSessionSeconds = 1800,
            isTimerRunning = false
        ),
//        KickSession(
//            id = 2,
//            date = System.currentTimeMillis() - 86400000,
//            mealType = "LUNCH",
//            kickCount = 8,
//            timerSeconds = 3600,
//            totalSessionSeconds = 3600,
//            isTimerRunning = false
//        )
    )
    MaterialTheme {
        HistoryScreenContent(sessions = sampleSessions)
    }
}
