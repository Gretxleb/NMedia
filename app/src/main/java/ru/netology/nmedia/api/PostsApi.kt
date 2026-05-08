package ru.netology.nmedia.api

import retrofit2.Response
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PushToken
import ru.netology.nmedia.dto.Token

object PostsApi {
    suspend fun getAll(): Response<List<Post>> = PostApi.getAll()
    suspend fun getNewer(id: Long): Response<List<Post>> = PostApi.getNewer(id)
    suspend fun save(post: Post): Response<Post> = PostApi.save(post)
    suspend fun update(id: Long, post: Post): Response<Post> = PostApi.update(id, post)
    suspend fun removeById(id: Long): Response<Unit> = PostApi.removeById(id)
    suspend fun likeById(id: Long): Response<Post> = PostApi.likeById(id)
    suspend fun unlikeById(id: Long): Response<Post> = PostApi.unlikeById(id)
    suspend fun updateUser(login: String, pass: String): Response<Token> = PostApi.updateUser(login, pass)
    suspend fun registerUser(login: String, pass: String, name: String): Response<Token> = PostApi.registerUser(login, pass, name)
    suspend fun sendPushToken(pushToken: PushToken): Response<Unit> = PostApi.sendPushToken(pushToken)
}