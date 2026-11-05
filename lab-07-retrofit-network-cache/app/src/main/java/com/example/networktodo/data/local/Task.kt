package com.example.networktodo.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Строка задачи, сохраняемая в таблице Room.
 *
 * @property id автоматически назначаемый первичный ключ.
 * @property title текст задачи.
 * @property isCompleted признак выполнения.
 * @property createdAt время создания в миллисекундах.
 * @property remoteId идентификатор задачи в JSONPlaceholder или null для локальной записи.
 * @property userId идентификатор владельца задачи из API или null для локальной записи.
 */
@Entity(tableName = "tasks", indices = [Index(value = ["remoteId"], unique = true)])
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val remoteId: Int? = null,
    val userId: Int? = null
)
