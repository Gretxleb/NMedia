package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity

class PostRepositoryRoomImpl(private val dao: PostDao) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = dao.getAll().map { list ->
        list.map { it.toDto() }
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }
}