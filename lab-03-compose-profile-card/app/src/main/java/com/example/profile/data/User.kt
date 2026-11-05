package com.example.profile.data

/**
 * Данные, отображаемые на экране профиля.
 *
 * @property name Имя пользователя, доступное для редактирования.
 * @property surname Фамилия пользователя.
 * @property status Краткое описание профиля.
 * @property followers Текущее количество подписчиков.
 * @property isSubscribed Состояние подписки текущего пользователя.
 * @author Быханов Михаил Сергеевич
 * @since 2026-11-05
 */
data class User(
    val name: String = "Михаил",
    val surname: String = "Быханов",
    val status: String = "Занимаюсь мобильной разработкой 📱",
    val followers: Int = 128,
    val isSubscribed: Boolean = false
)
