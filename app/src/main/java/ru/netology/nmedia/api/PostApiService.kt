package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.netology.nmedia.dto.Post

object PostApi {
    private val retrofit = Retrofit.Builder()
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

    private val service = retrofit.create(PostApiService::class.java)

    suspend fun getAll(): Response<List<Post>> = service.getAll()
    suspend fun getNewer(id: Long): Response<List<Post>> = service.getNewer(id)
    suspend fun save(post: Post): Response<Post> = service.save(post)
    suspend fun update(id: Long, post: Post): Response<Post> = service.update(id, post)
    suspend fun removeById(id: Long): Response<Unit> = service.removeById(id)
    suspend fun likeById(id: Long): Response<Post> = service.likeById(id)
    suspend fun unlikeById(id: Long): Response<Post> = service.unlikeById(id)
}

interface PostApiService {
    @GET("/api/posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("/api/posts/newer/{id}")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @POST("/api/posts")
    suspend fun save(@Body post: Post): Response<Post>

    @PUT("/api/posts/{id}")
    suspend fun update(@Path("id") id: Long, @Body post: Post): Response<Post>

    @DELETE("/api/posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("/api/posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @DELETE("/api/posts/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Long): Response<Post>
}
