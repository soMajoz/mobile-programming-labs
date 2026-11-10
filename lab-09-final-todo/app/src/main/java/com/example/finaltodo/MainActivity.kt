package com.example.finaltodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finaltodo.data.local.TaskDatabase
import com.example.finaltodo.data.remote.RetrofitClient
import com.example.finaltodo.data.repository.TaskRepository
import com.example.finaltodo.presentation.ui.FinalTodoTheme
import com.example.finaltodo.presentation.ui.TodoApp
import com.example.finaltodo.presentation.viewmodel.TaskViewModel
import com.example.finaltodo.presentation.viewmodel.TaskViewModelFactory

/** Android entry point that wires the database, repository, ViewModel, and Compose UI. */
class MainActivity : ComponentActivity() {

    /** Builds app dependencies and displays the task navigation graph. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = TaskRepository(
            taskDao = TaskDatabase.getInstance(applicationContext).taskDao(),
            apiService = RetrofitClient.apiService
        )
        setContent {
            SideEffect {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            }
            FinalTodoTheme {
                val taskViewModel: TaskViewModel = viewModel(
                    factory = TaskViewModelFactory(repository)
                )
                TodoApp(viewModel = taskViewModel)
            }
        }
    }
}
