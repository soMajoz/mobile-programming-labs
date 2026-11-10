package com.example.finaltodo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** Defines reactive reads and coroutine-based writes for the task table. */
@Dao
interface TaskDao {

    /** Observes all saved tasks, newest first. */
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC, id DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    /** Observes one task by its local key for the detail destination. */
    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    fun observeById(taskId: Long): Flow<TaskEntity?>

    /** Inserts a local task and returns its generated row identifier. */
    @Insert
    suspend fun insert(task: TaskEntity): Long

    /** Inserts or replaces imported API rows with the same remote identifier. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRemote(tasks: List<TaskEntity>)

    /** Persists the edited task fields. */
    @Update
    suspend fun update(task: TaskEntity)

    /** Removes a task from the local database. */
    @Delete
    suspend fun delete(task: TaskEntity)
}
