package com.example.profile.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.profile.ui.theme.ProfileTheme

/**
 * Кнопки подписки и сброса имени.
 *
 * @param isSubscribed Текущее состояние подписки.
 * @param onSubscribeClick Вызывается при подписке или отмене подписки.
 * @param onResetClick Вызывается для возврата имени по умолчанию.
 * @param modifier Модификатор расположения кнопок.
 * @return Ничего не возвращает; отображает две интерактивные кнопки.
 */
@Composable
fun ActionButtons(
    isSubscribed: Boolean,
    onSubscribeClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor by animateColorAsState(
        targetValue = if (isSubscribed) {
            MaterialTheme.colorScheme.tertiary
        } else {
            MaterialTheme.colorScheme.primary
        },
        label = "subscription_button_color"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onSubscribeClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(text = if (isSubscribed) "Отписаться" else "Подписаться")
        }
        OutlinedButton(
            onClick = onResetClick,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Сбросить")
        }
    }
}

/**
 * Предпросмотр кнопок в состоянии без подписки.
 *
 * @return Ничего не возвращает; создаёт превью ActionButtons.
 */
@Preview(showBackground = true)
@Composable
private fun ActionButtonsPreview() {
    ProfileTheme(darkTheme = false) {
        ActionButtons(
            isSubscribed = false,
            onSubscribeClick = {},
            onResetClick = {}
        )
    }
}
