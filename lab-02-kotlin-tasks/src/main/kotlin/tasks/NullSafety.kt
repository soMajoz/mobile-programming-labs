package tasks

/**
 * Примеры безопасной работы с nullable-значениями и коллекциями.
 *
 * @author Быханов Михаил Сергеевич
 * @version 1.0
 * @since 2026-11-05
 */
object NullSafety {
    /**
     * Безопасно преобразует строку в целое число.
     *
     * @param str Строка для преобразования или null.
     * @return Число, если строка корректна; иначе null.
     * Пример: parseIntSafe("123") возвращает 123.
     */
    fun parseIntSafe(str: String?): Int? = str?.toIntOrNull()

    /**
     * Удаляет null из списка и удваивает каждое оставшееся число.
     *
     * @param list Список целых чисел, элементы которого могут быть null.
     * @return Новый список без null, каждое число умножено на два.
     * Пример: filterNonNullAndDouble(listOf(1, null, 3)) возвращает [2, 6].
     */
    fun filterNonNullAndDouble(list: List<Int?>): List<Int> =
        list.filterNotNull().map { value -> value * 2 }
}
