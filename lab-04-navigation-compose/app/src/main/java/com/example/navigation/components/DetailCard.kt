package com.example.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.navigation.data.User
import com.example.navigation.ui.theme.NavigationTheme

/**
 * Отображает возраст, город и электронную почту профиля.
 *
 * @param user Пользователь, чьи данные нужно показать.
 * @param modifier Модификатор внешнего расположения карточки.
 */
@Composable
fun DetailCard(
    user: User,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            DetailRow(icon = Icons.Filled.Cake, label = "Возраст", value = "${user.age} лет")
            DetailRow(icon = Icons.Filled.LocationOn, label = "Город", value = user.city)
            DetailRow(icon = Icons.Filled.Email, label = "Email", value = user.email)
        }
    }
}

/**
 * Показывает одну строку описательных данных профиля.
 *
 * @param icon Значок, соответствующий типу данных.
 * @param label Название поля.
 * @param value Значение поля.
 */
@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * Предпросмотр карточки подробностей профиля.
 */
@Preview(showBackground = true)
@Composable
private fun DetailCardPreview() {
    NavigationTheme {
        DetailCard(User(1, "Алексей Смирнов", 25, "Москва", "alexey@example.com"))
    }
}
