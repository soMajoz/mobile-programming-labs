package com.example.navigation.data

/**
 * Данные пользователя, доступные на экранах списка и профиля.
 *
 * @property id Идентификатор пользователя, передаваемый через маршрут.
 * @property name Полное имя пользователя.
 * @property age Возраст пользователя в годах.
 * @property city Город проживания.
 * @property email Демонстрационный адрес электронной почты.
 */
data class User(
    val id: Int,
    val name: String,
    val age: Int,
    val city: String,
    val email: String
)

/**
 * Возвращает небольшой локальный набор демонстрационных профилей.
 *
 * @return Список из пяти пользователей.
 */
fun sampleUsers(): List<User> = listOf(
    User(1, "Алексей Смирнов", 25, "Москва", "alexey@example.com"),
    User(2, "Мария Иванова", 30, "Санкт-Петербург", "maria@example.com"),
    User(3, "Иван Петров", 22, "Казань", "ivan@example.com"),
    User(4, "Екатерина Сидорова", 28, "Новосибирск", "ekaterina@example.com"),
    User(5, "Дмитрий Козлов", 35, "Екатеринбург", "dmitry@example.com")
)
