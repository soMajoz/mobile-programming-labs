package com.example.counter.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CounterColors = lightColorScheme(
    primary = Color(0xFF315C68),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E8EF),
    onPrimaryContainer = Color(0xFF102E36),
    secondary = Color(0xFF4C625B),
    secondaryContainer = Color(0xFFD5E8DD),
    background = Color(0xFFF5F8F8),
    surface = Color(0xFFF5F8F8),
    surfaceVariant = Color(0xFFE0E9EB)
)

/** Applies the color palette and typography for the counter application. */
@Composable
fun CounterTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = CounterColors, typography = Typography(), content = content)
}