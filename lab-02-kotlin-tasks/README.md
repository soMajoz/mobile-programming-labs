# Kotlin Tasks — решение задач по Kotlin

## Описание
Проект содержит решения задач на синтаксис Kotlin, null-безопасность, коллекции, функции высшего порядка и корутины. В функции main находятся исполняемые проверки заданий и дополнительного разбора ввода.

## Технологии
- Kotlin/JVM 2.2.20
- Gradle Kotlin DSL 8.9
- kotlinx-coroutines-core 1.11.0
- JDK 17

## Сборка и запуск
На Windows:
```powershell
.\gradlew.bat run
```

На Linux/macOS:
```bash
./gradlew run
```

При несовпадении ожидаемого и фактического значения программа завершится с сообщением об ошибке.

Проверка 05.11.2026: команда clean build run завершилась успешно; в main прошло 24/24 проверки.

## Структура
- src/main/kotlin/Main.kt — точка входа и проверки решений.
- src/main/kotlin/tasks/BasicSyntax.kt — приветствие, сезон и факториал.
- src/main/kotlin/tasks/NullSafety.kt — nullable-значения и фильтрация списка.
- src/main/kotlin/tasks/Collections.kt — среднее, группировка и поиск.
- src/main/kotlin/tasks/Coroutines.kt — задержка и параллельные задачи.
- src/main/kotlin/tasks/UserInput.kt — дополнительный разбор чисел и текста.
- src/main/kotlin/tasks/DataClasses.kt — модель Person.
- src/main/kotlin/utils/TestUtils.kt — функции проверок.

## Особенности
runParallelTasks использует async и Deferred.onAwait, чтобы собрать результаты по фактическому порядку завершения, а затем получить их через await. Переполнение факториала явно сообщает об ошибке, поскольку точный результат не помещается в Long.

## Автор
Быханов Михаил Сергеевич, группа ПИН-б-о-24-1(1), курс 3.

## Лицензия
MIT. См. LICENSE.
