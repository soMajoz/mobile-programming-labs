package com.example.networktodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.networktodo.data.local.AppDatabase
import com.example.networktodo.data.remote.RetrofitClient
import com.example.networktodo.data.repository.TaskRepository
import com.example.networktodo.ui.TaskListScreen
import com.example.networktodo.ui.TaskTheme
import com.example.networktodo.viewmodel.TaskViewModel
import com.example.networktodo.viewmodel.TaskViewModelFactory

/** Entry point that connects Room, Repository, ViewModel and Compose. */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = TaskRepository(
            taskDao = AppDatabase.getInstance(applicationContext).taskDao(),
            apiService = RetrofitClient.apiService
        )
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
