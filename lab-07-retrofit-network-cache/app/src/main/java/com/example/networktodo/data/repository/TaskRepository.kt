package com.example.networktodo.data.repository

import com.example.networktodo.data.local.Task
import com.example.networktodo.data.local.TaskDao
import com.example.networktodo.data.remote.ApiService
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий объединяет локальную базу Room с JSONPlaceholder.
 *
 * @property taskDao DAO локального хранилища.
 * @property apiService клиент удалённого API.
 */
class TaskRepository(
    private val taskDao: TaskDao,
    private val apiService: ApiService
) {

    /** Возвращает реактивный список всех задач. */
    fun observeTasks(): Flow<List<Task>> = taskDao.observeAll()

    /** Downloads a page, maps the DTOs to entities, and upserts them into the cache. */
    suspend fun fetchTasksFromNetwork(page: Int, pageSize: Int): Boolean {
        val remoteTasks = apiService.getTasks(page, pageSize).map { dto ->
            Task(
                title = dto.title,
                isCompleted = dto.completed,
                remoteId = dto.id,
                userId = dto.userId
            )
        }
        taskDao.upsertRemoteTasks(remoteTasks)
        return remoteTasks.size == pageSize
    }

    /** Adds a local-only task; JSONPlaceholder does not persist write requests. */
    suspend fun addTask(title: String) {
        taskDao.insert(Task(title = title.trim()))
    }

    /** Saves an edited task in Room. */
    suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }

    /** Deletes a task from the local cache. */
    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    /** Toggles the completion state in Room. */
    suspend fun toggleCompletion(task: Task) {
        taskDao.update(task.copy(isCompleted = !task.isCompleted))
    }
}
