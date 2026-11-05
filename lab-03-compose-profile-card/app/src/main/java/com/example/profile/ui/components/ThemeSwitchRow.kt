package com.example.profile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.profile.ui.theme.ProfileTheme

/**
 * Переключатель светлой и тёмной цветовой схемы.
 *
 * @param darkTheme true, если выбрана тёмная тема.
 * @param onDarkThemeChange Обработчик нового значения переключателя.
 * @param modifier Модификатор расположения строки.
 * @return Ничего не возвращает; отображает подпись и переключатель.
 */
@Composable
fun ThemeSwitchRow(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (darkTheme) "Тёмная тема" else "Светлая тема",
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = darkTheme,
            onCheckedChange = onDarkThemeChange
        )
    }
}

/**
 * Предпросмотр переключателя темы.
 *
 * @return Ничего не возвращает; создаёт превью ThemeSwitchRow.
 */
@Preview(showBackground = true)
@Composable
private fun ThemeSwitchRowPreview() {
    ProfileTheme(darkTheme = false) {
        ThemeSwitchRow(darkTheme = false, onDarkThemeChange = {})
    }
}
