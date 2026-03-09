package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.model.Post
import java.util.concurrent.Executors

class PostRepositoryRoomImpl(
    private val dao: PostDao
) : PostRepository {

    private val executor = Executors.newSingleThreadExecutor()

    override val data: LiveData<List<Post>> = dao.getAll()

    override fun save(post: Post) {
        executor.execute {
            if (post.id == 0L) {
                dao.insert(post)
            } else {
                dao.update(post)
            }
        }
    }

    override fun removeById(id: Long) {
        executor.execute {
            dao.removeById(id)
        }
    }

    override fun likeById(id: Long) {
        executor.execute {
            dao.likeById(id)
        }
    }

    override fun shareById(id: Long) {
        executor.execute {
            dao.shareById(id)
        }
    }
}