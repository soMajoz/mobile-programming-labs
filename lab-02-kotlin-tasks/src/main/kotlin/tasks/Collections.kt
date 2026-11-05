package tasks

/**
 * Задачи на коллекции и функции высшего порядка Kotlin.
 *
 * @author Быханов Михаил Сергеевич
 * @version 1.0
 * @since 2026-11-05
 */
object Collections {
    /**
     * Вычисляет средний возраст людей в списке.
     *
     * @param people Список людей.
     * @return Средний возраст или 0.0, если список пуст.
     * Пример: для возрастов 25 и 30 возвращается 27.5.
     */
    fun averageAge(people: List<Person>): Double =
        if (people.isEmpty()) 0.0 else people.map { person -> person.age }.average()

    /**
     * Группирует непустые слова по первой букве в верхнем регистре.
     * Пустые строки пропускаются, поскольку у них нет первой буквы.
     *
     * @param words Список слов для группировки.
     * @return Карта от заглавной первой буквы к словам, начинающимся с неё.
     * Пример: listOf("apple", "banana") даст ключи 'A' и 'B'.
     */
    fun groupByFirstLetter(words: List<String>): Map<Char, List<String>> =
        words.filter { word -> word.isNotEmpty() }
            .groupBy { word -> word.first().uppercaseChar() }

    /**
     * Находит самое длинное слово.
     *
     * @param words Список слов.
     * @return Самое длинное слово или null, если список пуст.
     * Пример: findLongestWord(listOf("apple", "pineapple")) возвращает "pineapple".
     */
    fun findLongestWord(words: List<String>): String? =
        words.maxByOrNull { word -> word.length }
}
