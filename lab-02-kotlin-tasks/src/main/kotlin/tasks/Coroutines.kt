package tasks

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select

/**
 * Задачи на приостанавливаемые функции и структурированную конкурентность.
 *
 * @author Быханов Михаил Сергеевич
 * @version 1.0
 * @since 2026-11-05
 */
object Coroutines {
    /**
     * Печатает сообщение, приостанавливается на заданное время и печатает
     * сообщение о завершении.
     *
     * @param message Сообщение перед задержкой.
     * @param delayMs Длительность задержки в миллисекундах; должна быть неотрицательной.
     * @return Ничего не возвращает; выводит две строки в консоль.
     * @throws IllegalArgumentException Если задержка отрицательная.
     * Пример: delayedPrint("Начинаем...", 2000L) печатает сообщение, ждёт и выводит "Готово!".
     */
    suspend fun delayedPrint(message: String, delayMs: Long) {
        require(delayMs >= 0L) { "Задержка должна быть неотрицательной" }
        println(message)
        delay(delayMs)
        println("Готово!")
    }

    /**
     * Запускает три задачи одновременно и возвращает ответы в фактическом
     * порядке их завершения.
     *
     * @return Результаты трёх задач по мере завершения каждой из них.
     * Пример: при задержках 1, 2 и 3 секунды список начинается с "Результат 1".
     */
    suspend fun runParallelTasks(): List<String> = coroutineScope {
        val pendingTasks = mutableListOf(
            async {
                delay(1_000L)
                "Результат 1"
            },
            async {
                delay(2_000L)
                "Результат 2"
            },
            async {
                delay(3_000L)
                "Результат 3"
            }
        )
        val completionOrder = mutableListOf<String>()

        while (pendingTasks.isNotEmpty()) {
            val completedTask: Deferred<String> = select {
                pendingTasks.forEach { task ->
                    task.onAwait { task }
                }
            }
            completionOrder += completedTask.await()
            pendingTasks.remove(completedTask)
        }

        completionOrder
    }
}
