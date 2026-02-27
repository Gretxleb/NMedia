package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.model.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun getById(id: Long): LiveData<Post?>
    fun save(post: Post)
    fun edit(post: Post)
    fun removeById(id: Long)
    fun likeById(id: Long)
    fun shareById(id: Long)
}