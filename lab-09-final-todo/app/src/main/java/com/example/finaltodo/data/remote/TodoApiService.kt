package com.example.finaltodo.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/** Declares the read-only JSONPlaceholder task endpoint used for sample import. */
interface TodoApiService {

    /** Loads a bounded set of example tasks from the remote service. */
    @GET("todos")
    suspend fun getTasks(@Query("_limit") limit: Int): List<RemoteTask>
}
