package com.example.finaltodo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finaltodo.data.repository.TaskRepository

/** Creates TaskViewModel instances with their repository dependency. */
class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {

    /** Constructs the requested task ViewModel. */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(TaskViewModel::class.java))
        return TaskViewModel(repository) as T
    }
}
