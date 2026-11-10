package com.example.finaltodo.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finaltodo.data.local.TaskEntity
import com.example.finaltodo.presentation.viewmodel.TaskFilter
import com.example.finaltodo.presentation.viewmodel.TaskUiState
import com.example.finaltodo.presentation.viewmodel.TaskViewModel

/** Defines the routes used by the app's three destinations. */
object TodoRoutes {
    const val HOME = "home"
    const val TASKS = "tasks"
    const val TASK_ID = "taskId"
    const val DETAIL = "task/{taskId}"

    /** Builds a detail route for one local Room identifier. */
    fun detail(taskId: Long): String = "task/$taskId"
}

/** Hosts navigation and displays one-time status messages from the ViewModel. */
@Composable
fun TodoApp(viewModel: TaskViewModel) {
    val navController = rememberNavController()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val feedback = state.errorMessage ?: state.infoMessage

    LaunchedEffect(feedback) {
        if (feedback != null) {
            snackbarHostState.showSnackbar(feedback)
            viewModel.clearFeedback()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = TodoRoutes.HOME,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(TodoRoutes.HOME) {
                HomeScreen(
                    state = state,
                    onOpenTasks = { navController.navigate(TodoRoutes.TASKS) },
                    onOpenTask = { taskId -> navController.navigate(TodoRoutes.detail(taskId)) },
                    onRefresh = viewModel::refresh
                )
            }
            composable(TodoRoutes.TASKS) {
                TasksScreen(
                    state = state,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onOpenTask = { taskId -> navController.navigate(TodoRoutes.detail(taskId)) }
                )
            }
            composable(
                route = TodoRoutes.DETAIL,
                arguments = listOf(navArgument(TodoRoutes.TASK_ID) { type = NavType.LongType })
            ) { entry ->
                val taskId = entry.arguments?.getLong(TodoRoutes.TASK_ID) ?: return@composable
                TaskDetailScreen(
                    taskId = taskId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
}

/** Shows the overview, task counts, and shortcuts to the local task list. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: TaskUiState,
    onOpenTasks: () -> Unit,
    onOpenTask: (Long) -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("План на сегодня", style = MaterialTheme.typography.titleLarge)
                        Text("Мой список задач", style = MaterialTheme.typography.labelMedium)
                    }
                },
                actions = {
                    IconButton(onClick = onRefresh, enabled = !state.isRefreshing) {
                        if (state.isRefreshing) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = "Обновить примеры")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Маленькие шаги складываются в большой результат.",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Записывай планы, отмечай готовое и возвращайся к важному.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                SummaryCard(state = state, onOpenTasks = onOpenTasks)
            }
            item {
                Button(onClick = onOpenTasks, modifier = Modifier.fillMaxWidth().height(54.dp)) {
                    Icon(Icons.Default.TaskAlt, contentDescription = null)
                    Spacer(Modifier.width(10.dp))
                    Text("Открыть список задач")
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Недавние задачи", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onOpenTasks) { Text("Все") }
                }
            }
            if (state.tasks.isEmpty()) {
                item {
                    EmptyTasksCard(
                        title = if (state.isRefreshing) "Загружаем примеры задач" else "Список пока пуст",
                        subtitle = if (state.isRefreshing) "Скоро здесь появятся первые записи." else "Добавь первую задачу в список."
                    )
                }
            } else {
                items(state.tasks.take(3), key = TaskEntity::id) { task ->
                    TaskPreview(task = task, onClick = { onOpenTask(task.id) })
                }
            }
        }
    }
}

/** Shows the searchable task list with completion filters and an add-task action. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    state: TaskUiState,
    viewModel: TaskViewModel,
    onBack: () -> Unit,
    onOpenTask: (Long) -> Unit
) {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Задачи") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refresh, enabled = !state.isRefreshing) {
                        Icon(Icons.Default.Refresh, contentDescription = "Обновить")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Новая задача") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::setSearchQuery,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Поиск") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(18.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskFilter.entries.forEach { filter ->
                    FilterChip(
                        selected = state.selectedFilter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        label = { Text(filter.label) }
                    )
                }
            }
            if (state.isRefreshing && state.tasks.isNotEmpty()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Text(
                "${state.tasks.size} задач",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (state.tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    EmptyTasksCard(
                        title = if (state.isRefreshing) "Загружаем список" else "Ничего не найдено",
                        subtitle = "Можно добавить свою задачу — она сохранится на устройстве."
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 92.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.tasks, key = TaskEntity::id) { task ->
                        TaskRow(
                            task = task,
                            onToggle = { viewModel.toggleCompletion(task) },
                            onOpen = { onOpenTask(task.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        TaskEditorDialog(
            title = "Новая задача",
            initialTitle = "",
            initialNotes = "",
            onDismiss = { showAddDialog = false },
            onSave = { title, notes ->
                viewModel.addTask(title, notes)
                showAddDialog = false
            }
        )
    }
}

/** Displays completion counts and a simple progress bar for the dashboard. */
@Composable
private fun SummaryCard(state: TaskUiState, onOpenTasks: () -> Unit) {
    val progress = if (state.totalCount == 0) 0f else state.completedCount.toFloat() / state.totalCount
    Card(
        onClick = onOpenTasks,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Твой прогресс", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${state.completedCount} из ${state.totalCount} выполнено",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Box(
                    modifier = Modifier.size(54.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.35f))
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(progress.coerceIn(0f, 1f)).fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                CountLabel(label = "Всего", value = state.totalCount)
                CountLabel(label = "Активных", value = state.totalCount - state.completedCount)
                CountLabel(label = "Готово", value = state.completedCount)
            }
        }
    }
}

/** Shows one labeled task count on the dashboard card. */
@Composable
private fun CountLabel(label: String, value: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** Shows a compact empty state shared by the home and list screens. */
@Composable
private fun EmptyTasksCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.EventNote,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Renders a recent task as a tappable dashboard preview. */
@Composable
private fun TaskPreview(task: TaskEntity, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.TaskAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(task.title, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium)
                Text(
                    if (task.isCompleted) "Выполнено" else "В работе",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Открыть задачу", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

/** Renders one task row with a completion checkbox and a detail-screen action. */
@Composable
private fun TaskRow(task: TaskEntity, onToggle: () -> Unit, onOpen: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onOpen).padding(start = 7.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f).padding(vertical = 5.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = task.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (task.remoteId == null) "Локальная задача" else "Пример · JSONPlaceholder №${task.remoteId}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onOpen) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Подробности")
            }
        }
    }
}

/** Shows one task's details and actions for editing, completion, and deletion. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(taskId: Long, viewModel: TaskViewModel, onBack: () -> Unit) {
    val task by viewModel.observeTask(taskId).collectAsStateWithLifecycle(initialValue = null)
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("О задаче") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (task != null) {
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Изменить")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Удалить")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val currentTask = task
        if (currentTask == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyTasksCard(title = "Задача не найдена", subtitle = "Она могла быть удалена из списка.")
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 22.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    currentTask.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Описание", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Text(
                            currentTask.notes.ifBlank { "Описание не добавлено." },
                            style = MaterialTheme.typography.bodyLarge
                        )
                        HorizontalDivider()
                        Text(
                            if (currentTask.remoteId == null) "Создано на устройстве" else "Импортировано из JSONPlaceholder · №${currentTask.remoteId}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.height(2.dp))
                Button(
                    onClick = { viewModel.toggleCompletion(currentTask) },
                    modifier = Modifier.fillMaxWidth().height(54.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(10.dp))
                    Text(if (currentTask.isCompleted) "Вернуть в активные" else "Отметить выполненной")
                }
                Text(
                    if (currentTask.isCompleted) "Статус: выполнена" else "Статус: в работе",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showEditDialog) {
        task?.let { currentTask ->
            TaskEditorDialog(
                title = "Изменить задачу",
                initialTitle = currentTask.title,
                initialNotes = currentTask.notes,
                onDismiss = { showEditDialog = false },
                onSave = { title, notes ->
                    viewModel.updateTask(currentTask, title, notes)
                    showEditDialog = false
                }
            )
        }
    }
    if (showDeleteDialog) {
        task?.let { currentTask ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Удалить задачу?") },
                text = { Text("«${currentTask.title}» будет удалена с устройства.") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteTask(currentTask)
                        showDeleteDialog = false
                        onBack()
                    }) { Text("Удалить") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("Отмена") }
                }
            )
        }
    }
}

/** Provides a reusable dialog for adding or editing a task and its notes. */
@Composable
private fun TaskEditorDialog(
    title: String,
    initialTitle: String,
    initialNotes: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var taskTitle by rememberSaveable(initialTitle) { mutableStateOf(initialTitle) }
    var taskNotes by rememberSaveable(initialNotes) { mutableStateOf(initialNotes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Название") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = taskNotes,
                    onValueChange = { taskNotes = it },
                    label = { Text("Описание") },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(taskTitle.trim(), taskNotes.trim()) },
                enabled = taskTitle.isNotBlank()
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}
