package com.example.profile.ui.components

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
import androidx.compose.ui.unit.dp
import com.example.profile.ui.theme.ProfileTheme

/**
 * Отображает стандартную иконку человека внутри круглого аватара.
 *
 * @param modifier Модификатор размера и расположения аватара.
 * @return Ничего не возвращает; рисует круглый аватар.
 */
@Composable
fun Avatar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(116.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        tonalElevation = 3.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Аватар пользователя",
                modifier = Modifier.size(68.dp)
            )
        }
    }
}

/**
 * Предпросмотр круглого аватара.
 *
 * @return Ничего не возвращает; создаёт превью компонента Avatar.
 */
@Preview(showBackground = true)
@Composable
private fun AvatarPreview() {
    ProfileTheme(darkTheme = false) {
        Avatar()
    }
}
