package com.example.finaltodo.data.repository

import com.example.finaltodo.data.local.TaskDao
import com.example.finaltodo.data.local.TaskEntity
import com.example.finaltodo.data.remote.TodoApiService
import kotlinx.coroutines.flow.Flow

/** Coordinates local Room storage and the optional JSONPlaceholder import. */
class TaskRepository(
    private val taskDao: TaskDao,
    private val apiService: TodoApiService
) {

    /** Observes the full local task list. */
    fun observeTasks(): Flow<List<TaskEntity>> = taskDao.observeAll()

    /** Observes one local row for the task detail screen. */
    fun observeTask(taskId: Long): Flow<TaskEntity?> = taskDao.observeById(taskId)

    /** Downloads sample tasks and writes them into the local Room cache. */
    suspend fun refreshRemoteTasks(): Int {
        val remote = apiService.getTasks(limit = REMOTE_TASK_LIMIT).map { item ->
            TaskEntity(
                title = item.title,
                notes = "Импортировано из JSONPlaceholder. Изменения сохраняются только на устройстве.",
                isCompleted = item.completed,
                createdAt = System.currentTimeMillis() + item.id,
                remoteId = item.id,
                ownerId = item.userId
            )
        }
        taskDao.upsertRemote(remote)
        return remote.size
    }

    /** Creates a task that exists only in the local database. */
    suspend fun addTask(title: String, notes: String) {
        taskDao.insert(TaskEntity(title = title, notes = notes))
    }

    /** Saves the edited task in Room. */
    suspend fun updateTask(task: TaskEntity) {
        taskDao.update(task)
    }

    /** Removes one task from Room. */
    suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }

    /** Changes the completion flag and persists it. */
    suspend fun toggleCompletion(task: TaskEntity) {
        taskDao.update(task.copy(isCompleted = !task.isCompleted))
    }

    /** Holds the limit used for the remote demonstration import. */
    private companion object {
        const val REMOTE_TASK_LIMIT = 20
    }
}
