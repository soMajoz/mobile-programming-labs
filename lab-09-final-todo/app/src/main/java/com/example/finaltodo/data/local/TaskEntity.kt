package com.example.finaltodo.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Describes a task stored in the local Room database.
 *
 * @property id local primary key used by navigation.
 * @property title short task name shown in the list.
 * @property notes optional details shown on the task screen.
 * @property isCompleted whether the task has been completed.
 * @property createdAt creation time in epoch milliseconds.
 * @property remoteId optional JSONPlaceholder identifier for imported tasks.
 * @property ownerId optional owner identifier from JSONPlaceholder.
 */
@Entity(tableName = "tasks", indices = [Index(value = ["remoteId"], unique = true)])
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val notes: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val remoteId: Int? = null,
    val ownerId: Int? = null
)
