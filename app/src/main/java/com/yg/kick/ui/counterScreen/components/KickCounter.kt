package com.yg.kick.ui.counterScreen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes.Companion.Cookie12Sided
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun KickCounter(
    kickCount: Int,
    timerText: String,
    onKickIncrement: () -> Unit
) {
    // The main kick-counting button with a 12-sided cookie shape
    Button(
        onClick = onKickIncrement,
        modifier = Modifier.size(250.dp),
        shape = Cookie12Sided.toShape(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
    ) {
        Text(
            text = "$kickCount",
            fontSize = 130.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            lineHeight = 130.sp
        )
    }
    Spacer(modifier = Modifier.height(18.dp))
    Text(
        text = timerText,
        fontSize = 36.sp,
        color = Color.Black.copy(alpha = 0.8f)
    )
}
