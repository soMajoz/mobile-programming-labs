package com.example.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.counter.ui.CounterScreen
import com.example.counter.ui.CounterTheme

/** Entry point for the MVVM counter. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SideEffect {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            }
            CounterTheme {
                val counterViewModel: CounterViewModel = viewModel(factory = CounterViewModel.Factory)
                CounterScreen(viewModel = counterViewModel)
            }
        }
    }
}