package com.example.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.local.Task
import com.example.todo.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Координирует задачи Room, поисковую строку и выбранный порядок сортировки.
 *
 * @property repository источник задач.
 */
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val sortOption = MutableStateFlow(TaskSortOption.NEWEST)

    /** Наблюдаемое состояние списка задач. */
    val uiState: StateFlow<TaskUiState> = combine(
        repository.observeTasks(),
        searchQuery.asStateFlow(),
        sortOption.asStateFlow()
    ) { tasks, query, sort ->
        val filtered = tasks.filter { it.title.contains(query.trim(), ignoreCase = true) }
        val ordered = when (sort) {
            TaskSortOption.NEWEST -> filtered.sortedByDescending(Task::createdAt)
            TaskSortOption.ALPHABETICAL -> filtered.sortedBy { it.title.lowercase() }
            TaskSortOption.ACTIVE_FIRST -> filtered.sortedWith(
                compareBy<Task> { it.isCompleted }.thenByDescending(Task::createdAt)
            )
        }
        TaskUiState(
            tasks = ordered,
            searchQuery = query,
            sortOption = sort,
            totalCount = tasks.size,
            completedCount = tasks.count(Task::isCompleted)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TaskUiState())

    /** Updates the task-title filter. */
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    /** Selects a supported task ordering. */
    fun setSortOption(option: TaskSortOption) {
        sortOption.value = option
    }

    /** Adds a task with a trimmed, non-empty title. */
    fun addTask(title: String) {
        val cleanTitle = title.trim()
        if (cleanTitle.isNotEmpty()) viewModelScope.launch { repository.addTask(cleanTitle) }
    }

    /** Updates the edited task. */
    fun updateTask(task: Task) {
        val cleanTitle = task.title.trim()
        if (cleanTitle.isNotEmpty()) {
            viewModelScope.launch { repository.updateTask(task.copy(title = cleanTitle)) }
        }
    }

    /** Toggles the completed flag in Room. */
    fun toggleCompletion(task: Task) {
        viewModelScope.launch { repository.toggleCompletion(task) }
    }

    /** Deletes a task from Room. */
    fun deleteTask(task: Task) {
        viewModelScope.launch { repository.deleteTask(task) }
    }
}