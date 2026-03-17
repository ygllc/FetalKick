package com.yg.kick

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class Helpers private constructor() {
    object ui {
        object theme {
            private val darkTheme: Boolean @Composable get() = isSystemInDarkTheme()
            private val dynamicColor: Boolean = true

            @OptIn(ExperimentalMaterial3ExpressiveApi::class)
            val colorScheme
                @Composable get() = when {
                    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                        val context = LocalContext.current
                        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                            context
                        )
                    }

                    darkTheme -> darkColorScheme()
                    else -> expressiveLightColorScheme()
                }
        }
    }
}