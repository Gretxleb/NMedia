package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    override val data: Flow<List<Post>> = dao.getAll().map { it.map(PostEntity::toDto) }
    
    override val newerCount: Flow<Int> = dao.getNewerCount(0)

    override suspend fun getAll() {
        try {
            val response = PostApi.getAll()
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.map(PostEntity::fromDto))
        } catch (e: ApiError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getNewer() {
        try {
            val response = PostApi.getNewer(1)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.map { PostEntity.fromDto(it).copy(visible = false) })
        } catch (e: ApiError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = if (post.id == 0L) PostApi.save(post) else PostApi.update(post.id, post)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: ApiError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        val post = dao.getById(id)
        dao.removeById(id)
        try {
            val response = PostApi.removeById(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
        } catch (e: ApiError) {
            dao.insert(post)
            throw e
        } catch (e: IOException) {
            dao.insert(post)
            throw NetworkError
        } catch (e: Exception) {
            dao.insert(post)
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long) {
        val post = dao.getById(id)
        if (post.likedByMe) {
            dao.unlikeById(id)
        } else {
            dao.likeById(id)
        }
        try {
            val response = if (post.likedByMe) PostApi.unlikeById(id) else PostApi.likeById(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
        } catch (e: ApiError) {
            if (post.likedByMe) dao.likeById(id) else dao.unlikeById(id)
            throw e
        } catch (e: IOException) {
            if (post.likedByMe) dao.likeById(id) else dao.unlikeById(id)
            throw NetworkError
        } catch (e: Exception) {
            if (post.likedByMe) dao.likeById(id) else dao.unlikeById(id)
            throw UnknownError
        }
    }

    override suspend fun showAll() {
        dao.makeAllVisible()
    }
}
