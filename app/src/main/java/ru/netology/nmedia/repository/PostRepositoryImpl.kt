package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException

class PostRepositoryImpl(
    private val dao: PostDao,
    private val service: PostApiService
) : PostRepository {

    override val data: Flow<List<Post>> = dao.getAll().map { list -> list.map(PostEntity::toDto) }
    override val newerCount: Flow<Int> = dao.getNewerCount()

    override suspend fun getAll() {
        try {
            val response = service.getAll()
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.map { PostEntity.fromDto(it) })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getNewer() {
        try {
            val maxId = dao.getMaxId() ?: 0L
            val response = service.getNewer(maxId)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.map { PostEntity.fromDto(it, hidden = true) })
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = service.save(post)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        dao.removeById(id)
        try {
            val response = service.removeById(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long) {
        // Логика как в вашем текущем файле
    }

    override suspend fun showAll() {
        dao.showAll()
    }
}