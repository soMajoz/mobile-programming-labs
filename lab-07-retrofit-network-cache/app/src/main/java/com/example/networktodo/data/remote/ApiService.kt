package com.example.networktodo.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/** Retrofit contract for the JSONPlaceholder To-Do endpoint. */
interface ApiService {

    /** Fetches one page of remote tasks. */
    @GET("todos")
    suspend fun getTasks(
        @Query("_page") page: Int,
        @Query("_limit") pageSize: Int
    ): List<TaskDTO>
}
