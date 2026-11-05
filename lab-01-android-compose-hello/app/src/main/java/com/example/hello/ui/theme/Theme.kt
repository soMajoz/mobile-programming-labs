package com.example.hello.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HelloColorScheme = lightColorScheme(
    primary = Color(0xFF1E5AA8),
    onPrimary = Color.White,
    secondary = Color(0xFF526B88),
    background = Color(0xFFEFF4FA),
    surface = Color(0xFFEFF4FA)
)

/**
 * Цветовая тема приложения Hello Compose.
 *
 * @param content Интерфейс, который будет показан внутри темы.
 */
@Composable
fun HelloTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HelloColorScheme,
        content = content
    )
}
