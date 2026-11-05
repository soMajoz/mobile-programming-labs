package com.example.hello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hello.ui.theme.HelloTheme

/**
 * Главная Activity первого приложения на Jetpack Compose.
 * Устанавливает Compose-интерфейс с приветствием и данными студента.
 *
 * @author Быханов Михаил Сергеевич
 * @version 1.0
 * @since 2026-11-05
 */
class MainActivity : ComponentActivity() {

    /**
     * Создаёт Activity и устанавливает Compose-содержимое.
     *
     * @param savedInstanceState Сохранённое состояние Activity или null при первом запуске.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(name = "Быханов Михаил Сергеевич")
                }
            }
        }
    }
}

/**
 * Показывает название приложения и информацию о студенте.
 *
 * @param name ФИО студента, отображаемое на экране.
 * @param modifier Модификатор внешнего вида и расположения содержимого.
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEFF4FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Моё первое приложение",
            color = Color(0xFF17345C),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = name,
            modifier = Modifier.padding(top = 20.dp),
            color = Color(0xFF1E5AA8),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Группа ПИН-б-о-24-1(1)",
            modifier = Modifier.padding(top = 8.dp),
            color = Color(0xFF36465C),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Институт Перспективной Инженерии",
            modifier = Modifier.padding(top = 4.dp),
            color = Color(0xFF36465C),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Предпросмотр главного экрана в Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloTheme {
        Greeting(name = "Быханов Михаил Сергеевич")
    }
}
