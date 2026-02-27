package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.PostEntity
import ru.netology.nmedia.model.Post
import java.util.concurrent.Executors

class PostRepositoryRoomImpl(
    private val dao: PostDao
) : PostRepository {

    private val executor = Executors.newSingleThreadExecutor()

    override fun getAll(): LiveData<List<Post>> {
        return Transformations.map(dao.getAll()) { list ->
            list.map { it.toModel() }
        }
    }

    override fun getById(id: Long): LiveData<Post?> {
        return Transformations.map(dao.getById(id)) { it?.toModel() }
    }

    override fun save(post: Post) {
        executor.execute {
            val id = if (post.id == 0L) {
                val newId = System.currentTimeMillis()
                val entity = PostEntity.fromModel(post.copy(id = newId))
                dao.insert(entity)
            } else {
                val entity = PostEntity.fromModel(post)
                dao.insert(entity)
            }
        }
    }

    override fun edit(post: Post) {
        executor.execute {
            val existing = dao.getByIdOnce(post.id)
            if (existing != null) {
                val updated = PostEntity.fromModel(post)
                dao.update(updated)
            } else {
                dao.insert(PostEntity.fromModel(post))
            }
        }
    }

    override fun removeById(id: Long) {
        executor.execute {
            dao.deleteById(id)
        }
    }

    override fun likeById(id: Long) {
        executor.execute {
            val entity = dao.getByIdOnce(id) ?: return@execute
            val liked = !entity.likedByMe
            val likes = if (liked) entity.likes + 1 else entity.likes - 1
            val updated = PostEntity(
                id = entity.id,
                author = entity.author,
                content = entity.content,
                published = entity.published,
                likedByMe = liked,
                likes = likes,
                shares = entity.shares,
                video = entity.video
            )
            dao.update(updated)
        }
    }

    override fun shareById(id: Long) {
        executor.execute {
            val entity = dao.getByIdOnce(id) ?: return@execute
            val updated = entity.copy(shares = entity.shares + 1)
            dao.update(updated)
        }
    }
}