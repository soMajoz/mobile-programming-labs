package com.example.networktodo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.networktodo.data.repository.TaskRepository

/** Creates TaskViewModel with the repository dependency. */
class TaskViewModelFactory(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {

    /** Constructs a TaskViewModel for the requested class. */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(TaskViewModel::class.java))
        return TaskViewModel(repository) as T
    }
}