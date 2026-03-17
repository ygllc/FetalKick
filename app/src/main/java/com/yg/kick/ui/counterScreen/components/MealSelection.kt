package com.yg.kick.ui.counterScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.yg.kick.ui.counterScreen.viewModels.MealType

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MealSelection(
    selectedMeal: MealType,
    onMealSelected: (Int) -> Unit
) {
    val mealOptions = listOf("Breakfast", "Lunch", "Snacks", "Dinner")

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
    ) {
        mealOptions.forEachIndexed { index, label ->
            ToggleButton(
                checked = selectedMeal.ordinal == index,
                onCheckedChange = { onMealSelected(index) },
                modifier = Modifier.weight(1f),
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    mealOptions.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                },
                colors = ToggleButtonDefaults.toggleButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.tertiary,
                    checkedContainerColor = MaterialTheme.colorScheme.tertiary,
                    checkedContentColor = Color.White
                )
            ) {
                Text(text = label, fontSize = 10.sp, maxLines = 1)
            }
        }
    }
}
