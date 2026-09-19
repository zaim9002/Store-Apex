package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val ApexDarkColorScheme = darkColorScheme(
    primary = ApexPrimary,
    onPrimary = ApexOnPrimary,
    primaryContainer = ApexPrimaryContainer,
    onPrimaryContainer = ApexPrimary,
    secondary = ApexSecondary,
    onSecondary = ApexTextPrimary,
    secondaryContainer = ApexSecondaryContainer,
    tertiary = ApexTertiary,
    background = ApexBackground,
    onBackground = ApexTextPrimary,
    surface = ApexSurface,
    onSurface = ApexTextPrimary,
    surfaceVariant = ApexSurfaceVariant,
    onSurfaceVariant = ApexTextSecondary,
    outline = ApexBorder,
    error = ApexRed
)

@Composable
fun ApexStoreTheme(
    isRtl: Boolean = true,
    content: @Composable () -> Unit
) {
    val layoutDirection = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        MaterialTheme(
            colorScheme = ApexDarkColorScheme,
            typography = Typography,
            content = content
        )
    }
}

