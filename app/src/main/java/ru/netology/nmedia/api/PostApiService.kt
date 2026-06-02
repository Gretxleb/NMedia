package ru.netology.nmedia.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Token

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

    @FormUrlEncoded
    @POST("/api/users/authentication")
    suspend fun updateUser(
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<Token>

    @FormUrlEncoded
    @POST("/api/users/registration")
    suspend fun registerUser(
        @Field("login") login: String,
        @Field("pass") pass: String,
        @Field("name") name: String
    ): Response<Token>
}

