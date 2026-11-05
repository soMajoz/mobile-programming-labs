package com.example.networktodo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networktodo.data.local.Task
import com.example.networktodo.data.repository.TaskRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * Координирует задачи Room, поисковую строку и выбранный порядок сортировки.
 *
 * @property repository источник задач.
 */
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private companion object {
        const val PAGE_SIZE = 20
    }

    private val searchQuery = MutableStateFlow("")
    private val sortOption = MutableStateFlow(TaskSortOption.NEWEST)
    private val networkState = MutableStateFlow(
        NetworkState(isLoading = true, isRefreshing = true)
    )
    private var nextPage = 1
    private var lastRequestedPage = 1
    private var lastRequestResetsPages = true
    private var requestJob: Job? = null

    private val taskContent = combine(
        repository.observeTasks(),
        searchQuery.asStateFlow(),
        sortOption.asStateFlow()
    ) { tasks, query, sort ->
        val filtered = tasks.filter { it.title.contains(query.trim(), ignoreCase = true) }
        val ordered = when (sort) {
            TaskSortOption.NEWEST -> filtered.sortedWith(
                compareBy<Task> { it.remoteId != null }
                    .thenBy { task -> task.remoteId?.toLong() ?: -task.createdAt }
            )
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
    }

    /** Combines the Room-backed list with network progress and error state. */
    val uiState: StateFlow<TaskUiState> = combine(taskContent, networkState.asStateFlow()) { content, network ->
        content.copy(
            isLoading = network.isLoading,
            isRefreshing = network.isRefreshing,
            isLoadingMore = network.isLoadingMore,
            hasMorePages = network.hasMorePages,
            errorMessage = network.errorMessage
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TaskUiState(isLoading = true, isRefreshing = true)
    )

    init {
        requestPage(page = 1, resetPages = true, initialLoad = true)
    }

    /** Updates the task-title filter. */
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    /** Selects a supported task ordering. */
    fun setSortOption(option: TaskSortOption) {
        sortOption.value = option
    }

    /** Refreshes the first network page while the UI continues observing Room. */
    fun refresh() {
        requestPage(page = 1, resetPages = true, initialLoad = false)
    }

    /** Loads the next JSONPlaceholder page and appends it to the local cache. */
    fun loadNextPage() {
        if (networkState.value.hasMorePages) {
            requestPage(page = nextPage, resetPages = false, initialLoad = false)
        }
    }

    /** Retries the most recent page after an error. */
    fun retryLastRequest() {
        requestPage(
            page = lastRequestedPage,
            resetPages = lastRequestResetsPages,
            initialLoad = false
        )
    }

    /** Clears a message after the Snackbar has been dismissed. */
    fun clearError() {
        networkState.value = networkState.value.copy(errorMessage = null)
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

    private fun requestPage(page: Int, resetPages: Boolean, initialLoad: Boolean) {
        if (requestJob?.isActive == true) return
        lastRequestedPage = page
        lastRequestResetsPages = resetPages
        networkState.value = networkState.value.copy(
            isLoading = initialLoad,
            isRefreshing = true,
            isLoadingMore = !resetPages && !initialLoad,
            errorMessage = null
        )
        requestJob = viewModelScope.launch {
            try {
                val hasMore = repository.fetchTasksFromNetwork(page, PAGE_SIZE)
                nextPage = if (resetPages) 2 else page + 1
                networkState.value = networkState.value.copy(hasMorePages = hasMore)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (exception: HttpException) {
                networkState.value = networkState.value.copy(
                    errorMessage = "Сервер вернул ошибку HTTP ${exception.code()}."
                )
            } catch (exception: IOException) {
                networkState.value = networkState.value.copy(
                    errorMessage = "Нет соединения. Сохранённые задачи доступны офлайн."
                )
            } catch (exception: Exception) {
                networkState.value = networkState.value.copy(
                    errorMessage = "Не удалось загрузить задачи. Попробуйте ещё раз."
                )
            } finally {
                networkState.value = networkState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    isLoadingMore = false
                )
            }
        }
    }
}

private data class NetworkState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMorePages: Boolean = true,
    val errorMessage: String? = null
)
