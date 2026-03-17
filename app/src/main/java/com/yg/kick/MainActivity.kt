package com.yg.kick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yg.kick.ui.counterScreen.screens.BreakfastScreen
import com.yg.kick.ui.counterScreen.viewModels.KickCounterViewModel
import com.yg.kick.ui.counterScreen.viewModels.KickCounterViewModelFactory
import com.yg.kick.ui.historyScreen.HistoryScreen
import com.yg.kick.ui.theme.FetalKickTheme
import com.yg.kick.ui.theme.robotoFlexTopAppBar

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val authManager = AuthManager(this)

        setContent {
            FetalKickTheme({
                val viewModel: KickCounterViewModel = viewModel(factory = KickCounterViewModelFactory(application))
                val uiState by viewModel.uiState.collectAsState()
                
                var checked by remember { mutableStateOf(false) }
                var userEmail by rememberSaveable { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(false) }
//                val scope = rememberCoroutineScope()
//                val webClientId = stringResource(R.string.google_web_client_id)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    "Fetal Kick",
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = robotoFlexTopAppBar
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { /* TODO */ }) {
                                    Icon(
                                        Icons.Default.MoreVert,
                                        contentDescription = "More options",
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            },
                            actions = {
                                if (userEmail != null) {
                                    IconButton(onClick = { userEmail = null }) {
                                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                                    }
                                }
                                IconButton(onClick = { viewModel.navigateToHistory() }) {
                                    Icon(
                                        Icons.Rounded.History,
                                        contentDescription = "History",
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
//                                Switch(
//                                    checked = checked,
//                                    onCheckedChange = { checked = it },
//                                    thumbContent = if (checked) {
//                                        {
//                                            Icon(
//                                                imageVector = Icons.Rounded.Check,
//                                                contentDescription = null,
//                                                modifier = Modifier.size(SwitchDefaults.IconSize),
//                                            )
//                                        }
//                                    } else {
//                                        null
//                                    },
//                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        if (isLoading) {
                            CircularWavyProgressIndicator()
                        } else {
                            when (uiState.currentScreen) {
                                com.yg.kick.ui.counterScreen.viewModels.Screen.COUNTER -> {
                                    BreakfastScreen(viewModel = viewModel)
                                }
                                com.yg.kick.ui.counterScreen.viewModels.Screen.HISTORY -> {
                                    HistoryScreen(
                                        onNavigateBack = { viewModel.navigateToCounter() }
                                    )
                                }
                            }
                        }
                    }
                }
            })
        }
    }
}

