package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Apex Store Premium Dark Obsidian & Electric Blue Theme (matching reference design)
val ApexBackground = Color(0xFF090D16)
val ApexSurface = Color(0xFF0E1424)
val ApexSurfaceVariant = Color(0xFF1C263D)
val ApexSurfaceCard = Color(0xFF131B2E)

val ApexPrimary = Color(0xFF3B82F6)           // Vibrant Royal Blue
val ApexPrimaryGradientStart = Color(0xFF4F46E5) // Indigo
val ApexPrimaryGradientEnd = Color(0xFF3B82F6)   // Royal Blue
val ApexOnPrimary = Color(0xFFFFFFFF)
val ApexPrimaryContainer = Color(0xFF1E3A8A)

val ApexSecondary = Color(0xFF8B5CF6)         // Electric Violet
val ApexSecondaryContainer = Color(0xFF3B1D82)

val ApexTertiary = Color(0xFF10B981)          // Emerald Green
val ApexAmber = Color(0xFFFBBF24)             // Warm Gold for Stars & Ratings
val ApexRed = Color(0xFFEF4444)               // Crimson for Delete/Errors

val ApexTextPrimary = Color(0xFFF8FAFC)
val ApexTextSecondary = Color(0xFF94A3B8)
val ApexTextMuted = Color(0xFF64748B)

val ApexBorder = Color(0xFF222F47)
val ApexDivider = Color(0xFF172033)

val ApexPrimaryBrush = Brush.horizontalGradient(
    listOf(ApexPrimaryGradientStart, ApexPrimaryGradientEnd)
)

