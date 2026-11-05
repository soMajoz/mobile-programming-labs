package com.example.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.profile.data.User
import com.example.profile.ui.components.ActionButtons
import com.example.profile.ui.components.Avatar
import com.example.profile.ui.components.ProfileInfo
import com.example.profile.ui.components.ThemeSwitchRow
import com.example.profile.ui.theme.ProfileTheme

/**
 * Собирает адаптивный экран профиля и хранит интерактивное состояние.
 *
 * @param user Начальные данные профиля.
 * @param darkTheme Текущее состояние тёмной темы.
 * @param onDarkThemeChange Обработчик переключения темы.
 * @param modifier Модификатор всего экрана.
 * @return Ничего не возвращает; отображает профиль и его действия.
 */
@Composable
fun ProfileScreen(
    user: User = User(),
    darkTheme: Boolean = false,
    onDarkThemeChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable(user.name) { mutableStateOf(user.name) }
    var isSubscribed by rememberSaveable(user.isSubscribed) {
        mutableStateOf(user.isSubscribed)
    }
    var followers by rememberSaveable(user.followers) {
        mutableIntStateOf(user.followers)
    }
    var clickCount by rememberSaveable { mutableIntStateOf(0) }
    val displayedUser = user.copy(
        name = name,
        followers = followers,
        isSubscribed = isSubscribed
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ThemeSwitchRow(
            darkTheme = darkTheme,
            onDarkThemeChange = onDarkThemeChange,
            modifier = Modifier.widthIn(max = 520.dp)
        )
        Text(
            text = "Профиль",
            modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Card(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Avatar()
                ProfileInfo(user = displayedUser, modifier = Modifier.fillMaxWidth())
                HorizontalDivider()
                TextField(
                    value = name,
                    onValueChange = { newName -> name = newName.take(40) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Имя") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    )
                )
                ActionButtons(
                    isSubscribed = isSubscribed,
                    onSubscribeClick = {
                        if (isSubscribed) {
                            followers = (followers - 1).coerceAtLeast(user.followers)
                        } else {
                            followers += 1
                        }
                        isSubscribed = !isSubscribed
                    },
                    onResetClick = { name = user.name }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Количество нажатий: $clickCount",
                        modifier = Modifier.padding(end = 12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    androidx.compose.material3.FilledTonalButton(
                        onClick = { clickCount += 1 }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Увеличить счётчик",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(text = "+")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(4.dp))
    }
}

/**
 * Предпросмотр экрана профиля со светлой темой и начальными данными.
 *
 * @return Ничего не возвращает; создаёт превью ProfileScreen.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileTheme(darkTheme = false) {
        ProfileScreen()
    }
}
