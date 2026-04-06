package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.netology.nmedia.dto.Post

val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:9999")
    .addConverterFactory(GsonConverterFactory.create())
    .client(
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    )
    .build()

val PostApi: PostApiService = retrofit.create(PostApiService::class.java)

interface PostApiService {
    @GET("/api/posts")
    fun getAll(): Call<List<Post>>

    @POST("/api/posts")
    fun save(@Body post: Post): Call<Post>

    @PUT("/api/posts/{id}")
    fun update(@Path("id") id: Long, @Body post: Post): Call<Post>

    @DELETE("/api/posts/{id}")
    fun removeById(@Path("id") id: Long): Call<Unit>

    @POST("/api/posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Call<Post>

    @DELETE("/api/posts/{id}/likes")
    fun unlikeById(@Path("id") id: Long): Call<Post>
}