package com.example.profile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

private val LightColors = lightColorScheme(
    primary = Color(0xFF3559A8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE2FF),
    onPrimaryContainer = Color(0xFF00174A),
    secondary = Color(0xFF565E7B),
    tertiary = Color(0xFF7B5265),
    background = Color(0xFFF8F7FC),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE4E2EC)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB7C4FF),
    onPrimary = Color(0xFF1B2E69),
    primaryContainer = Color(0xFF334780),
    onPrimaryContainer = Color(0xFFDCE2FF),
    secondary = Color(0xFFC1C6E0),
    tertiary = Color(0xFFEDB8CF),
    background = Color(0xFF111318),
    surface = Color(0xFF1A1B21),
    surfaceVariant = Color(0xFF444650)
)

/**
 * Применяет цветовую схему профиля для светлой или тёмной темы.
 *
 * @param darkTheme true для тёмной палитры, false для светлой.
 * @param content Содержимое, к которому применяется тема.
 * @return Ничего не возвращает; предоставляет MaterialTheme дочерним элементам.
 */
@Composable
fun ProfileTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content
    )
}

/**
 * Предпросмотр типографики и цветов темы профиля.
 *
 * @return Ничего не возвращает; создаёт превью светлой темы.
 */
@Preview(showBackground = true)
@Composable
private fun ProfileThemePreview() {
    ProfileTheme(darkTheme = false) {
        MaterialTheme.colorScheme.let { colors ->
            androidx.compose.material3.Surface(color = colors.background) {
                androidx.compose.material3.Text(
                    text = "Тема профиля",
                    color = colors.onBackground
                )
            }
        }
    }
}
