package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import java.io.IOException

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data: LiveData<List<Post>> = dao.getAll().map { it.toDto() }

    override suspend fun getAll() {
        try {
            val response = PostsApi.service.getAll()
            if (!response.isSuccessful) {
                throw RuntimeException("Error: ${response.code()}")
            }
            val body = response.body() ?: throw RuntimeException("Empty body")
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw RuntimeException("Network error")
        }
    }

    override suspend fun likeById(id: Long) {
        try {
            val post = data.value?.find { it.id == id } ?: return
            val response = if (post.likedByMe) {
                PostsApi.service.unlikeById(id)
            } else {
                PostsApi.service.likeById(id)
            }
            if (!response.isSuccessful) {
                throw RuntimeException("Error: ${response.code()}")
            }
            val body = response.body() ?: throw RuntimeException("Empty body")
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw RuntimeException("Network error")
        }
    }

    override suspend fun removeById(id: Long) {
        try {
            val response = PostsApi.service.removeById(id)
            if (!response.isSuccessful) {
                throw RuntimeException("Error: ${response.code()}")
            }
            dao.removeById(id)
        } catch (e: IOException) {
            throw RuntimeException("Network error")
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.service.save(post)
            if (!response.isSuccessful) {
                throw RuntimeException("Error: ${response.code()}")
            }
            val body = response.body() ?: throw RuntimeException("Empty body")
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw RuntimeException("Network error")
        }
    }

    override suspend fun shareById(id: Long) {
        dao.shareById(id)
    }
}
