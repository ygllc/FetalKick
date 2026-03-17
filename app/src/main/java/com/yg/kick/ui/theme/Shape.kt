package com.yg.kick.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable

object MyMileageShapeDefaults {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun topListItemShape(): RoundedCornerShape =
        RoundedCornerShape(
            topStart = shapes.large.topStart,
            topEnd = shapes.large.topEnd,
            bottomStart = shapes.extraSmall.bottomStart,
            bottomEnd = shapes.extraSmall.bottomEnd
        )

    @Composable
    fun middleListItemShape(): RoundedCornerShape =
        RoundedCornerShape(shapes.extraSmall.topStart)

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun bottomListItemShape(): RoundedCornerShape =
        RoundedCornerShape(
            topStart = shapes.extraSmall.topStart,
            topEnd = shapes.extraSmall.topEnd,
            bottomStart = shapes.large.bottomStart,
            bottomEnd = shapes.large.bottomEnd
        )
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    val cardShape: CornerBasedShape
        @Composable get() = shapes.largeIncreased
}
