# Отчёт по лабораторной работе №4

## Информация о студенте
- ФИО: Быханов Михаил Сергеевич
- Группа: ПИН-б-о-24-1(1)
- Дата выполнения: 05.11.2026

## Цель
Изучить жизненный цикл Activity и реализовать навигацию между экранами Jetpack Compose с передачей данных.

## Выполненная работа
1. Создан экран списка из пяти демонстрационных пользователей на основе `LazyColumn`.
2. Созданы переиспользуемые карточки пользователя, подробностей профиля и круглый аватар.
3. Настроен `NavHost` с маршрутами списка и профиля.
4. При выборе карточки передаётся целочисленный `userId` через `navArgument` типа `NavType.IntType`.
5. Детальный экран показывает имя, возраст, город и демонстрационный email; неизвестный ID обрабатывается отдельным сообщением.
6. Добавлена кнопка «Назад» через `popBackStack()`.
7. Добавлены `@Preview` экранов и компонентов.
8. Основные callback-и Activity записывают события жизненного цикла в Logcat с тегом `NavigationLifecycle`.

## Проверка
- `.\gradlew.bat clean build --console=plain` — `BUILD SUCCESSFUL`; собраны debug и release, `lint` завершился успешно.
- Приложение установлено и запущено на эмуляторе Pixel 6 с Android API 35.
- Проверены открытие профиля Алексей Смирнов (ID 1), возврат к списку и открытие Марии Ивановой (ID 2); данные на деталях соответствуют выбранным ID.
- В Logcat подтверждены `onCreate`, `onStart`, `onResume`, `onPause` и `onStop` при сворачивании и возвращении в приложение.
- Снимки экранов сохранены в `screenshots/list_screen.png` и `screenshots/detail_screen.png`.

## Сложности и решения
- Методичка приводит Navigation Compose 2.7.7. Более новая версия 2.10.2 требует AGP 9.1 и compileSdk 37, поэтому оставлена совместимая версия из задания для проекта с API 35.
- Чтобы избежать передачи больших объектов через маршрут, экран списка передаёт только числовой ID, а детальный экран находит пользователя в демонстрационном наборе.

## Вывод
Реализовано многоэкранное приложение на одной Activity. NavController меняет текущий маршрут, NavHost связывает маршрут с Compose-экраном, а аргумент userId позволяет найти нужную запись и отобразить её данные.

## Источники
1. Navigation Compose: https://developer.android.com/develop/ui/compose/navigation
2. Жизненный цикл Activity: https://developer.android.com/guide/components/activities/activity-lifecycle
3. Lazy lists: https://developer.android.com/develop/ui/compose/lists
4. Выпуски AndroidX Navigation: https://developer.android.com/jetpack/androidx/releases/navigation
