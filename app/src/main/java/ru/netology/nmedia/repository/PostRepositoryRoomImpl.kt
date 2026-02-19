package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryRoomImpl(private val dao: PostDao) : PostRepository {
    override val data: LiveData<List<Post>> = dao.getAll().map { list ->
        list.map { it.toDto() }
    }

    override fun likeById(id: Long) {
        val post = data.value?.find { it.id == id } ?: return
        if (post.likedByMe) {
            dao.unlikeById(id)
        } else {
            dao.likeById(id)
        }
    }

    override fun shareById(id: Long) = dao.shareById(id)

    override fun removeById(id: Long) = dao.removeById(id)

    override fun save(post: Post) {
        dao.insert(PostEntity.fromDto(post))
    }
}