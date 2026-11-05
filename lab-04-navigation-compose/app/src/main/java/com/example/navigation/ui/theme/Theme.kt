package com.example.navigation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

private val LightColors = lightColorScheme(
    primary = Color(0xFF3159A5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE2FF),
    onPrimaryContainer = Color(0xFF00174A),
    secondary = Color(0xFF535E7B),
    background = Color(0xFFF8F7FC),
    surface = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF2F0F7)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB7C4FF),
    onPrimary = Color(0xFF1B2E69),
    primaryContainer = Color(0xFF334780),
    onPrimaryContainer = Color(0xFFDCE2FF),
    secondary = Color(0xFFC1C6E0),
    background = Color(0xFF111318),
    surface = Color(0xFF1A1B21),
    surfaceContainerLow = Color(0xFF24252C)
)

/**
 * Предоставляет Material 3 палитру экранам приложения.
 *
 * @param darkTheme Признак использования тёмной цветовой схемы.
 * @param content Дочерний Compose-интерфейс.
 */
@Composable
fun NavigationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content
    )
}

/**
 * Предпросмотр компонентов в цветовой теме приложения.
 */
@Preview(showBackground = true)
@Composable
private fun NavigationThemePreview() {
    NavigationTheme {
        MaterialTheme {
            androidx.compose.material3.Text(text = "Navigation App")
        }
    }
}
