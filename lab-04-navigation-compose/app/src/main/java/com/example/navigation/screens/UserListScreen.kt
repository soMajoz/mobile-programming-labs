package com.example.navigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.navigation.components.UserCard
import com.example.navigation.data.sampleUsers
import com.example.navigation.ui.theme.NavigationTheme

/**
 * Экран прокручиваемого списка пользователей.
 *
 * @param onUserClick Обработчик выбора пользователя по идентификатору.
 * @param modifier Модификатор внешнего расположения экрана.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    onUserClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val users = remember { sampleUsers() }
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Список пользователей") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Выберите профиль для просмотра",
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items(items = users, key = { user -> user.id }) { user ->
                UserCard(
                    user = user,
                    onClick = { onUserClick(user.id) }
                )
            }
        }
    }
}

/**
 * Предпросмотр списка без привязки к навигационному контроллеру.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun UserListScreenPreview() {
    NavigationTheme {
        UserListScreen(onUserClick = {})
    }
}
