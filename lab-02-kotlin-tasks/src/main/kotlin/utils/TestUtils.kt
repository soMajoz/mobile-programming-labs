package utils

import kotlinx.coroutines.CancellationException

/**
 * Небольшие проверки для запуска примеров лабораторной из main().
 *
 * @author Быханов Михаил Сергеевич
 * @version 1.0
 * @since 2026-11-05
 */
object TestUtils {
    private var passed = 0
    private var total = 0

    /**
     * Сравнивает ожидаемое и фактическое значения, останавливая запуск при ошибке.
     *
     * @param label Название проверки.
     * @param expected Ожидаемое значение.
     * @param actual Фактическое значение.
     * @return Ничего не возвращает; печатает статус проверки.
     */
    fun assertEquals(label: String, expected: Any?, actual: Any?) {
        total += 1
        check(expected == actual) {
            "$label: ожидалось <$expected>, получено <$actual>"
        }
        passed += 1
        println("✓ $label")
    }

    /**
     * Проверяет произвольное логическое условие.
     *
     * @param label Название проверки.
     * @param condition Выражение, которое должно быть истинным.
     * @return Ничего не возвращает; печатает статус проверки.
     */
    fun assertTrue(label: String, condition: Boolean) {
        total += 1
        check(condition) { "$label: условие ложно" }
        passed += 1
        println("✓ $label")
    }

    /**
     * Проверяет, что suspend-блок завершается без исключения.
     *
     * @param label Название проверки.
     * @param block Приостанавливаемый блок, который проверяется.
     * @return Ничего не возвращает; печатает статус проверки.
     * @throws IllegalStateException Если блок завершается с ошибкой.
     */
    suspend fun assertCompletes(label: String, block: suspend () -> Unit) {
        total += 1
        try {
            block()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (failure: Exception) {
            throw IllegalStateException("$label: блок завершился с ошибкой", failure)
        }
        passed += 1
        println("✓ $label")
    }

    /**
     * Печатает число успешно выполненных проверок.
     *
     * @return Ничего не возвращает; выводит итог в консоль.
     */
    fun printSummary() {
        println("Пройдено проверок: $passed/$total")
    }
}
