@file:OptIn(ExperimentalTextApi::class)

package com.yg.kick.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import com.yg.kick.R
import com.yg.kick.ui.theme.AppFonts.googleFlex400
import com.yg.kick.ui.theme.AppFonts.googleFlex600

// Set of Material typography styles to start with
val RobotoFlex = FontFamily(Font(R.font.roboto_flex_variable))
// Set of Material typography styles to start with
val TYPOGRAPHY = Typography()
val Typography = Typography(
    displayLarge = TYPOGRAPHY.displayLarge.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    displayMedium = TYPOGRAPHY.displayMedium.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    displaySmall = TYPOGRAPHY.displaySmall.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    headlineLarge = TYPOGRAPHY.headlineLarge.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    headlineMedium = TYPOGRAPHY.headlineMedium.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    headlineSmall = TYPOGRAPHY.headlineSmall.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    titleLarge = TYPOGRAPHY.titleLarge.copy(
        fontFamily = googleFlex400,
        fontFeatureSettings = "ss02, dlig"
    ),
    titleMedium = TYPOGRAPHY.titleMedium.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    titleSmall = TYPOGRAPHY.titleSmall.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    bodyLarge = TYPOGRAPHY.bodyLarge.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    bodyMedium = TYPOGRAPHY.bodyMedium.copy(
        fontFamily = googleFlex400,
        fontFeatureSettings = "ss02, dlig"
    ),
    bodySmall = TYPOGRAPHY.bodySmall.copy(
        fontFamily = googleFlex400,
        fontFeatureSettings = "ss02, dlig"
    ),
    labelLarge = TYPOGRAPHY.labelLarge.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    labelMedium = TYPOGRAPHY.labelMedium.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    ),
    labelSmall = TYPOGRAPHY.labelSmall.copy(
        fontFamily = googleFlex600,
        fontFeatureSettings = "ss02, dlig"
    )
)
/* Other default text styles to override **/
val robotoFlexTopAppBar = FontFamily(
    Font(
        R.font.roboto_flex_variable,
        variationSettings = FontVariation.Settings(
            FontVariation.width(125f),
            FontVariation.weight(1000),
            FontVariation.grade(0), // <-- Int, not Float
            FontVariation.Setting("XOPQ", 96F),
            FontVariation.Setting("XTRA", 500f),
            FontVariation.Setting("YOPQ", 79f),
            FontVariation.Setting("YTAS", 750f),
            FontVariation.Setting("YTDE", -203f),
            FontVariation.Setting("YTFI", 738f),
            FontVariation.Setting("YTLC", 514f),
            FontVariation.Setting("YTUC", 712f)
        )
    )
)

object AppFonts {
    val googleFlex400 = FontFamily(Font(R.font.google_sans_flex_600))
    val googleFlex600 = FontFamily(Font(R.font.google_sans_flex_variable))
    val robotoMonoBold = FontFamily(Font(R.font.roboto_mono_bold))

}