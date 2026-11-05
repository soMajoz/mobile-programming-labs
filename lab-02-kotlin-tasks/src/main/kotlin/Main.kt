import kotlinx.coroutines.runBlocking
import tasks.BasicSyntax
import tasks.Collections
import tasks.Coroutines
import tasks.NullSafety
import tasks.Person
import tasks.UserInput
import utils.TestUtils

/**
 * Точка входа и проверка решений лабораторной работы №2.
 *
 * @return Ничего не возвращает; запускает примеры и печатает результаты.
 * @author Быханов Михаил Сергеевич
 * @version 1.0
 * @since 2026-11-05
 */
fun main() = runBlocking {
    println("=== Лабораторная работа №2: задачи Kotlin ===")

    println("\n--- Блок 1. Базовый синтаксис ---")
    TestUtils.assertEquals(
        "Приветствие и возраст",
        "Привет, Анна! Через 10 лет тебе будет 35 лет.",
        BasicSyntax.greetUser("Анна", 25)
    )
    TestUtils.assertEquals("Весна", "Весна", BasicSyntax.getSeason(3))
    TestUtils.assertEquals(
        "Некорректный месяц",
        "Некорректный месяц",
        BasicSyntax.getSeason(13)
    )
    TestUtils.assertEquals("Факториал 5", 120L, BasicSyntax.factorial(5))
    TestUtils.assertEquals("Факториал 0", 1L, BasicSyntax.factorial(0))
    TestUtils.assertEquals("Отрицательный факториал", -1L, BasicSyntax.factorial(-3))
    TestUtils.assertEquals(
        "Максимальный факториал в Long",
        2_432_902_008_176_640_000L,
        BasicSyntax.factorial(20)
    )
    TestUtils.assertTrue("Переполнение факториала выявляется", runCatching {
        BasicSyntax.factorial(21)
    }.exceptionOrNull() is ArithmeticException)

    println("\n--- Блок 2. Null-безопасность ---")
    TestUtils.assertEquals("Строка с числом", 123, NullSafety.parseIntSafe("123"))
    TestUtils.assertEquals("Строка без числа", null, NullSafety.parseIntSafe("abc"))
    TestUtils.assertEquals("Nullable-ввод", null, NullSafety.parseIntSafe(null))
    TestUtils.assertEquals(
        "Фильтрация null и удвоение",
        listOf(2, 6, 10),
        NullSafety.filterNonNullAndDouble(listOf(1, null, 3, null, 5))
    )

    println("\n--- Блок 3. Коллекции ---")
    val people = listOf(
        Person("Анна", 25),
        Person("Иван", 30),
        Person("Мария", 35)
    )
    TestUtils.assertEquals("Средний возраст", 30.0, Collections.averageAge(people))
    TestUtils.assertEquals(
        "Средний возраст пустого списка",
        0.0,
        Collections.averageAge(emptyList())
    )
    TestUtils.assertEquals(
        "Группировка по заглавной первой букве",
        mapOf('A' to listOf("apple", "apricot"), 'B' to listOf("banana")),
        Collections.groupByFirstLetter(listOf("apple", "apricot", "banana"))
    )
    TestUtils.assertEquals(
        "Пустые строки не дают ключ группировки",
        mapOf('A' to listOf("apple")),
        Collections.groupByFirstLetter(listOf("", "apple"))
    )
    TestUtils.assertEquals(
        "Самое длинное слово",
        "pineapple",
        Collections.findLongestWord(listOf("apple", "banana", "pineapple"))
    )
    TestUtils.assertEquals("Пустой список слов", null, Collections.findLongestWord(emptyList()))

    println("\n--- Блок 4. Корутины ---")
    TestUtils.assertCompletes("Асинхронная задержка завершается") {
        Coroutines.delayedPrint("Начинаем задержку на 2 секунды...", 2_000L)
    }
    val completionOrder = Coroutines.runParallelTasks()
    println("Порядок завершения: $completionOrder")
    TestUtils.assertEquals(
        "Три задачи завершились в порядке задержек",
        listOf("Результат 1", "Результат 2", "Результат 3"),
        completionOrder
    )

    println("\n--- Дополнительное задание ---")
    TestUtils.assertEquals(
        "Сводка чисел",
        mapOf(
            "sum" to 15L,
            "average" to 3.0,
            "count" to 5,
            "sorted" to listOf(1, 2, 3, 4, 5)
        ),
        UserInput.processUserInput("1,2,3,4,5")
    )
    TestUtils.assertEquals(
        "Сводка текста",
        mapOf("wordCount" to 3, "longestWord" to "пишет", "charCount" to 12),
        UserInput.processUserInput("Паша пишет код")
    )
    TestUtils.assertEquals(
        "Пустая строка",
        mapOf("wordCount" to 0, "longestWord" to "", "charCount" to 0),
        UserInput.processUserInput("   ")
    )
    TestUtils.assertEquals(
        "Отсутствующий ввод",
        mapOf("error" to "Ввод отсутствует"),
        UserInput.processUserInput(null)
    )

    println()
    TestUtils.printSummary()
}
