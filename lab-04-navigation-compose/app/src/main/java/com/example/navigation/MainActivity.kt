package com.example.navigation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.navigation.navigation.NavGraph
import com.example.navigation.ui.theme.NavigationTheme

/**
 * Единственная Activity приложения, которая размещает Compose-навигацию.
 */
class MainActivity : ComponentActivity() {

    /**
     * Создаёт интерфейс и записывает событие создания Activity в Logcat.
     *
     * @param savedInstanceState Состояние Activity, сохранённое системой.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycle("onCreate")
        setContent {
            NavigationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }

    /** Записывает в Logcat переход Activity в видимое состояние. */
    override fun onStart() {
        super.onStart()
        logLifecycle("onStart")
    }

    /** Записывает в Logcat получение Activity фокуса. */
    override fun onResume() {
        super.onResume()
        logLifecycle("onResume")
    }

    /** Записывает в Logcat потерю Activity фокуса. */
    override fun onPause() {
        logLifecycle("onPause")
        super.onPause()
    }

    /** Записывает в Logcat скрытие Activity. */
    override fun onStop() {
        logLifecycle("onStop")
        super.onStop()
    }

    /** Записывает в Logcat уничтожение Activity. */
    override fun onDestroy() {
        logLifecycle("onDestroy")
        super.onDestroy()
    }

    private fun logLifecycle(event: String) {
        Log.d(TAG, event)
    }

    private companion object {
        const val TAG = "NavigationLifecycle"
    }
}
