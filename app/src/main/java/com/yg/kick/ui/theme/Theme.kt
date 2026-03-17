@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.yg.kick.ui.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import com.yg.kick.Helpers

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FetalKickTheme(
    content: @Composable () -> Unit
) {
    MaterialExpressiveTheme(
        colorScheme = Helpers.ui.theme.colorScheme,
        typography = Typography,
        motionScheme = MotionScheme.expressive(),
        content = content,
    )
}