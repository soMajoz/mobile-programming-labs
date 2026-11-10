package com.example.finaltodo.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/** Configures Retrofit and the JSON converter for JSONPlaceholder. */
object RetrofitClient {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private val contentType = "application/json; charset=UTF-8".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    /** Returns the ready-to-use Retrofit service. */
    val apiService: TodoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(TodoApiService::class.java)
    }
}
