package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    override val data: Flow<List<Post>> = flow {
        emit(dao.getAll().map { it.toDto() })
    }

    override suspend fun likeById(id: Long) {
        dao.likeById(id)
        try {
            PostsApi.service.likeById(id)
        } catch (_: Exception) {
            dao.likeById(id)
        }
    }

    override suspend fun removeById(id: Long) {
        dao.removeById(id)
        try {
            PostsApi.service.removeById(id)
        } catch (_: Exception) {
        }
    }

    override suspend fun save(post: Post) {
        dao.insert(PostEntity.fromDto(post))
        try {
            val response = PostsApi.service.save(post)
            val body = response.body() ?: return
            dao.insert(PostEntity.fromDto(body))
        } catch (_: Exception) {
        }
    }

    fun getNewer(id: Long): Flow<Int> = flow {
        emit(0)
    }
}