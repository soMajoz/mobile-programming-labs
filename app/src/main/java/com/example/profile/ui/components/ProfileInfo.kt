package com.example.profile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.profile.data.User
import com.example.profile.ui.theme.ProfileTheme

/**
 * Показывает имя, статус и количество подписчиков пользователя.
 *
 * @param user Данные профиля для отображения.
 * @param modifier Модификатор расположения блока.
 * @return Ничего не возвращает; отображает сведения профиля.
 */
@Composable
fun ProfileInfo(
    user: User,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${user.name} ${user.surname}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = user.status,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Подписчиков: ${user.followers}",
            modifier = Modifier.padding(top = 10.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Предпросмотр информации профиля с демонстрационными данными.
 *
 * @return Ничего не возвращает; создаёт превью ProfileInfo.
 */
@Preview(showBackground = true)
@Composable
private fun ProfileInfoPreview() {
    ProfileTheme(darkTheme = false) {
        ProfileInfo(user = User(), modifier = Modifier.padding(16.dp))
    }
}
