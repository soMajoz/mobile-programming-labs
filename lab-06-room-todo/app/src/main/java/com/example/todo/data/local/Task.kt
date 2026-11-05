package com.example.todo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Строка задачи, сохраняемая в таблице Room.
 *
 * @property id автоматически назначаемый первичный ключ.
 * @property title текст задачи.
 * @property isCompleted признак выполнения.
 * @property createdAt время создания в миллисекундах.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)