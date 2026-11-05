package com.example.counter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.counter.data.CounterState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Хранит состояние счётчика и выполняет действия пользователя.
 * Счёт, шаг и история сохраняются в SavedStateHandle, StateFlow публикует
 * неизменяемое состояние для Compose.
 */
class CounterViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _state = MutableStateFlow(
        CounterState(
            count = savedStateHandle.get<Int>(KEY_COUNT) ?: 0,
            step = savedStateHandle.get<Int>(KEY_STEP) ?: 1,
            history = savedStateHandle.get<IntArray>(KEY_HISTORY)?.toList() ?: emptyList(),
            isLoading = savedStateHandle.get<Boolean>(KEY_INITIALIZED) != true
        )
    )

    /** Текущее состояние для подписки UI. */
    val state: StateFlow<CounterState> = _state.asStateFlow()

    init {
        if (savedStateHandle.get<Boolean>(KEY_INITIALIZED) != true) loadInitialValue()
    }

    /** Увеличивает значение на выбранный шаг, не превышая максимум. */
    fun increment() {
        if (_state.value.count < _state.value.maxValue) {
            updateState { record(it.copy(count = (it.count + it.step).coerceAtMost(it.maxValue))) }
        }
    }

    /** Уменьшает значение на выбранный шаг, не опускаясь ниже нуля. */
    fun decrement() {
        if (_state.value.count > 0) {
            updateState { record(it.copy(count = (it.count - it.step).coerceAtLeast(0))) }
        }
    }

    /** Сбрасывает счёт и очищает историю изменений. */
    fun reset() {
        updateState { it.copy(count = 0, history = emptyList()) }
    }

    /** Устанавливает шаг из поддерживаемого набора 1, 5, 10 или 25. */
    fun setStep(newStep: Int) {
        if (newStep in ALLOWED_STEPS) updateState { it.copy(step = newStep) }
    }

    /** Асинхронно загружает начальное значение 42, если пользователь ещё не менял счёт. */
    fun loadInitialValue() {
        viewModelScope.launch {
            delay(800)
            if (_state.value.count == 0) updateState { record(it.copy(count = INITIAL_VALUE)) }
            updateState { it.copy(isLoading = false) }
            savedStateHandle[KEY_INITIALIZED] = true
        }
    }

    private fun updateState(transform: (CounterState) -> CounterState) {
        _state.update(transform)
        val current = _state.value
        savedStateHandle[KEY_COUNT] = current.count
        savedStateHandle[KEY_STEP] = current.step
        savedStateHandle[KEY_HISTORY] = current.history.toIntArray()
    }

    private fun record(state: CounterState): CounterState =
        state.copy(history = (state.history + state.count).takeLast(HISTORY_LIMIT))

    companion object {
        private const val KEY_COUNT = "counter_count"
        private const val KEY_STEP = "counter_step"
        private const val KEY_HISTORY = "counter_history"
        private const val KEY_INITIALIZED = "counter_initialized"
        private const val INITIAL_VALUE = 42
        private const val HISTORY_LIMIT = 8
        private val ALLOWED_STEPS = setOf(1, 5, 10, 25)

        /** Supplies the saved-state handle owned by the current Activity. */
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                require(modelClass.isAssignableFrom(CounterViewModel::class.java))
                return CounterViewModel(extras.createSavedStateHandle()) as T
            }
        }
    }
}