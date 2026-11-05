package com.example.todo.data.repository

import com.example.todo.data.local.Task
import com.example.todo.data.local.TaskDao
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий инкапсулирует доступ ViewModel к базе Room.
 *
 * @property taskDao DAO локального хранилища.
 */
class TaskRepository(private val taskDao: TaskDao) {

    /** Возвращает реактивный список всех задач. */
    fun observeTasks(): Flow<List<Task>> = taskDao.observeAll()

    /** Добавляет непустую задачу с текущей датой создания. */
    suspend fun addTask(title: String) {
        taskDao.insert(Task(title = title.trim()))
    }

    /** Сохраняет изменения задачи. */
    suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }

    /** Удаляет задачу из базы. */
    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    /** Инвертирует признак выполнения задачи. */
    suspend fun toggleCompletion(task: Task) {
        taskDao.update(task.copy(isCompleted = !task.isCompleted))
    }
}