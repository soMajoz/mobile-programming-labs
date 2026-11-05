package com.example.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Асинхронные операции чтения и изменения задач в Room.
 */
@Dao
interface TaskDao {

    /** Наблюдает за всем списком; Room повторно публикует его при изменении таблицы. */
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Task>>

    /** Добавляет новую задачу и возвращает назначенный ключ. */
    @Insert
    suspend fun insert(task: Task): Long

    /** Сохраняет изменённое название или состояние задачи. */
    @Update
    suspend fun update(task: Task)

    /** Удаляет указанную задачу. */
    @Delete
    suspend fun delete(task: Task)
}