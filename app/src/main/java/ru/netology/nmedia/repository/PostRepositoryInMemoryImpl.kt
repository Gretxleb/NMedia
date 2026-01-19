package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Иван Иванов",
        content = "Привет! Это код на MVVM без комментариев.",
        published = "19 января в 12:00",
        likedByMe = false,
        likes = 999,
        shares = 995
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(
            likedByMe = !post.likedByMe,
            likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
        )
        data.value = post
    }

    override fun share() {
        post = post.copy(shares = post.shares + 1)
        data.value = post
    }
}