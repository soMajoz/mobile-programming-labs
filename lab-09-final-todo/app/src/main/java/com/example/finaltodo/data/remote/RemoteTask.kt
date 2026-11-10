package com.example.finaltodo.data.remote

import kotlinx.serialization.Serializable

/** Describes the task payload returned by the JSONPlaceholder API. */
@Serializable
data class RemoteTask(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
