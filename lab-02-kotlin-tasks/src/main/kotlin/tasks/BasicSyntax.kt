package tasks

/**
 * Набор задач по базовому синтаксису Kotlin.
 *
 * @author Быханов Михаил Сергеевич
 * @version 1.0
 * @since 2026-11-05
 */
object BasicSyntax {
    /**
     * Формирует приветствие с возрастом пользователя через десять лет.
     *
     * @param name Имя пользователя.
     * @param age Текущий возраст пользователя.
     * @return Строка приветствия с вычисленным возрастом.
     * Пример: greetUser("Анна", 25) возвращает приветствие с возрастом 35 лет.
     */
    fun greetUser(name: String, age: Int): String =
        "Привет, $name! Через 10 лет тебе будет ${age + 10} лет."

    /**
     * Возвращает название времени года для номера месяца.
     *
     * @param month Номер месяца от 1 до 12.
     * @return Название сезона или сообщение о некорректном месяце.
     * Пример: getSeason(3) возвращает "Весна".
     */
    fun getSeason(month: Int): String = when (month) {
        12, 1, 2 -> "Зима"
        3, 4, 5 -> "Весна"
        6, 7, 8 -> "Лето"
        9, 10, 11 -> "Осень"
        else -> "Некорректный месяц"
    }

    /**
     * Вычисляет факториал неотрицательного числа хвостовой рекурсией.
     * При переполнении Long точное умножение бросает ArithmeticException.
     *
     * @param n Число, для которого вычисляется факториал.
     * @param accumulator Накопленное произведение для хвостовой рекурсии.
     * @return Факториал или -1, если n отрицательно.
     * @throws ArithmeticException Если точный результат не помещается в Long.
     * Пример: factorial(5) возвращает 120.
     */
    tailrec fun factorial(n: Int, accumulator: Long = 1L): Long = when {
        n < 0 -> -1L
        n <= 1 -> accumulator
        else -> factorial(n - 1, Math.multiplyExact(accumulator, n.toLong()))
    }
}
