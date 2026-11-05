package com.example.networktodo.viewmodel

import com.example.networktodo.data.local.Task

/** Порядок сортировки, доступный в списке задач. */
enum class TaskSortOption(val label: String) {
    NEWEST("Сначала новые"),
    ALPHABETICAL("По алфавиту"),
    ACTIVE_FIRST("Сначала активные")
}

/** Состояние, которое ViewModel передаёт Compose-интерфейсу. */
data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val sortOption: TaskSortOption = TaskSortOption.NEWEST,
    val totalCount: Int = 0,
    val completedCount: Int = 0,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMorePages: Boolean = true,
    val errorMessage: String? = null
)
