package com.example.counter.data

/**
 * Неизменяемое состояние экрана счётчика.
 *
 * @property count Текущее значение.
 * @property step Шаг изменения.
 * @property maxValue Максимально допустимое значение.
 * @property history Последние значения счётчика.
 * @property isLoading Признак загрузки стартового значения.
 */
data class CounterState(
    val count: Int = 0,
    val step: Int = 1,
    val maxValue: Int = 100,
    val history: List<Int> = emptyList(),
    val isLoading: Boolean = true
)