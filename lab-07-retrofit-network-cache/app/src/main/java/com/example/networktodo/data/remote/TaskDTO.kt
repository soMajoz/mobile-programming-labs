package com.example.networktodo.data.remote

import kotlinx.serialization.Serializable

/**
 * JSONPlaceholder task shape used at the network boundary.
 *
 * @property userId owner identifier returned by the API.
 * @property id stable remote identifier used to upsert the Room cache.
 * @property title task text returned by the API.
 * @property completed remote completion state.
 */
@Serializable
data class TaskDTO(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
