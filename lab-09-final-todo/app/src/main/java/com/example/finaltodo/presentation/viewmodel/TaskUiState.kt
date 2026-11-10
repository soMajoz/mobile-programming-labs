package com.example.finaltodo.presentation.viewmodel

import com.example.finaltodo.data.local.TaskEntity

/** Filters that can be applied to the task list. */
enum class TaskFilter(val label: String) {
    ALL("Все"),
    ACTIVE("Активные"),
    COMPLETED("Готовые")
}

/** Immutable state rendered by the Compose screens. */
data class TaskUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: TaskFilter = TaskFilter.ALL,
    val totalCount: Int = 0,
    val completedCount: Int = 0,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)
