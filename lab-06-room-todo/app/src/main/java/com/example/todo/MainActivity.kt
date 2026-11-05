package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.data.local.AppDatabase
import com.example.todo.data.repository.TaskRepository
import com.example.todo.ui.TaskListScreen
import com.example.todo.ui.TaskTheme
import com.example.todo.viewmodel.TaskViewModel
import com.example.todo.viewmodel.TaskViewModelFactory

/** Entry point that connects Room, Repository, ViewModel and Compose. */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = TaskRepository(AppDatabase.getInstance(applicationContext).taskDao())
        setContent {
            SideEffect {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            }
            TaskTheme {
                val taskViewModel: TaskViewModel = viewModel(
                    factory = TaskViewModelFactory(repository)
                )
                TaskListScreen(viewModel = taskViewModel)
            }
        }
    }
}