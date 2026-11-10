package com.example.finaltodo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finaltodo.data.local.TaskEntity
import com.example.finaltodo.data.repository.TaskRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/** Holds presentation state and sends task actions to the repository. */
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val selectedFilter = MutableStateFlow(TaskFilter.ALL)
    private val requestState = MutableStateFlow(RequestState())

    private val taskContent = combine(
        repository.observeTasks(),
        searchQuery.asStateFlow(),
        selectedFilter.asStateFlow()
    ) { tasks, query, filter ->
        val matchingTasks = tasks.filter { task ->
            val matchesQuery = query.isBlank() || task.title.contains(query.trim(), ignoreCase = true)
            val matchesFilter = when (filter) {
                TaskFilter.ALL -> true
                TaskFilter.ACTIVE -> !task.isCompleted
                TaskFilter.COMPLETED -> task.isCompleted
            }
            matchesQuery && matchesFilter
        }
        TaskUiState(
            tasks = matchingTasks,
            searchQuery = query,
            selectedFilter = filter,
            totalCount = tasks.size,
            completedCount = tasks.count(TaskEntity::isCompleted)
        )
    }

    /** Exposes Room data, search, filters, and request feedback as one StateFlow. */
    val uiState: StateFlow<TaskUiState> = combine(
        taskContent,
        requestState.asStateFlow()
    ) { content, request ->
        content.copy(
            isRefreshing = request.isRefreshing,
            errorMessage = request.errorMessage,
            infoMessage = request.infoMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TaskUiState()
    )

    init {
        refresh()
    }

    /** Updates the case-insensitive task search query. */
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    /** Selects which completion states appear in the list. */
    fun setFilter(filter: TaskFilter) {
        selectedFilter.value = filter
    }

    /** Retries the optional remote import while Room continues to provide the visible list. */
    fun refresh() {
        if (requestState.value.isRefreshing) return
        requestState.value = RequestState(isRefreshing = true)
        viewModelScope.launch {
            try {
                val importedCount = repository.refreshRemoteTasks()
                requestState.value = RequestState(infoMessage = "Загружено примеров: $importedCount")
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (exception: HttpException) {
                requestState.value = RequestState(
                    errorMessage = "Сервер вернул ошибку HTTP ${exception.code()}. Локальные задачи доступны."
                )
            } catch (exception: IOException) {
                requestState.value = RequestState(
                    errorMessage = "Нет соединения. Локальные задачи доступны офлайн."
                )
            } catch (exception: Exception) {
                requestState.value = RequestState(
                    errorMessage = "Не удалось обновить примеры. Локальные задачи сохранены."
                )
            } finally {
                requestState.value = requestState.value.copy(isRefreshing = false)
            }
        }
    }

    /** Clears an error or success message after it has been displayed. */
    fun clearFeedback() {
        requestState.value = requestState.value.copy(errorMessage = null, infoMessage = null)
    }

    /** Adds a validated task to the local database. */
    fun addTask(title: String, notes: String) {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) return
        viewModelScope.launch { repository.addTask(cleanTitle, notes.trim()) }
    }

    /** Saves edited title and notes for a task. */
    fun updateTask(task: TaskEntity, title: String, notes: String) {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) return
        viewModelScope.launch {
            repository.updateTask(task.copy(title = cleanTitle, notes = notes.trim()))
        }
    }

    /** Toggles completion for the selected task. */
    fun toggleCompletion(task: TaskEntity) {
        viewModelScope.launch { repository.toggleCompletion(task) }
    }

    /** Deletes the selected task from Room. */
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch { repository.deleteTask(task) }
    }

    /** Observes a specific Room row for the task details destination. */
    fun observeTask(taskId: Long) = repository.observeTask(taskId)
}

/** Stores the progress and message for one remote request. */
private data class RequestState(
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)
