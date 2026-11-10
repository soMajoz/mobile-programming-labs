package com.example.finaltodo.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FinalTodoColors = lightColorScheme(
    primary = Color(0xFF385D4A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD2E8D8),
    onPrimaryContainer = Color(0xFF102F20),
    secondary = Color(0xFF5A6357),
    secondaryContainer = Color(0xFFDDE6D6),
    background = Color(0xFFF6F7F2),
    surface = Color(0xFFF6F7F2),
    surfaceVariant = Color(0xFFE5E9E0),
    error = Color(0xFFBA1A1A)
)

/** Applies the warm green Material 3 palette used by the final task app. */
@Composable
fun FinalTodoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FinalTodoColors,
        typography = Typography(),
        content = content
    )
}
