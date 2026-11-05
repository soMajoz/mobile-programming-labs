package tasks

/**
 * Обработка дополнительного задания по разбору ввода пользователя.
 *
 * @author Быханов Михаил Сергеевич
 * @version 1.0
 * @since 2026-11-05
 */
object UserInput {
    /**
     * Анализирует список целых чисел либо текст.
     *
     * Числовой ввод возвращает sum (Long), average (Double), count (Int) и
     * sorted (List<Int>). Для текста возвращаются wordCount, longestWord и
     * charCount; пробелы и прочие whitespace-символы не считаются.
     *
     * @param input Строка с целыми числами через запятую, текст или null.
     * @return Карта с числовой сводкой, текстовой сводкой или ключом error для null.
     * Пример: processUserInput("1,2,3")["sum"] возвращает 6L.
     */
    fun processUserInput(input: String?): Map<String, Any> {
        if (input == null) {
            return mapOf("error" to "Ввод отсутствует")
        }

        val normalizedInput = input.trim()
        val numberTokens = normalizedInput.split(',')
        val numbers = numberTokens.map { token -> token.trim().toIntOrNull() }
        val isIntegerList = normalizedInput.isNotEmpty() &&
            numbers.all { number -> number != null }

        if (isIntegerList) {
            val values = numbers.filterNotNull()
            val sum = values.sumOf { value -> value.toLong() }
            return mapOf(
                "sum" to sum,
                "average" to (sum.toDouble() / values.size),
                "count" to values.size,
                "sorted" to values.sorted()
            )
        }

        val words = normalizedInput
            .split(Regex("\\s+"))
            .filter { word -> word.isNotEmpty() }
        return mapOf(
            "wordCount" to words.size,
            "longestWord" to (words.maxByOrNull { word -> word.length } ?: ""),
            "charCount" to normalizedInput.count { character -> !character.isWhitespace() }
        )
    }
}
