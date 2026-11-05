package com.example.navigation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.navigation.ui.theme.NavigationTheme

/**
 * Показывает нейтральный круглый аватар-заглушку.
 *
 * @param modifier Модификатор размера и расположения аватара.
 * @param size Размер круглой области.
 * @param contentDescription Описание изображения для средств доступности.
 */
@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    contentDescription: String = "Аватар пользователя"
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = contentDescription,
                modifier = Modifier.size(size * 0.58f),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Предпросмотр аватара пользователя.
 */
@Preview(showBackground = true)
@Composable
private fun UserAvatarPreview() {
    NavigationTheme {
        UserAvatar()
    }
}
