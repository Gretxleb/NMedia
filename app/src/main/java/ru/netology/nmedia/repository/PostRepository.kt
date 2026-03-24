package ru.netology.nmedia.repository

import ru.netology.nmedia.model.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeById(id: Long, callback: Callback<Post>)
    fun removeById(id: Long, callback: Callback<Unit>)
    fun save(post: Post, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(result: T)
        fun onError(e: Exception)
    }
}
