package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    val loading: LiveData<Boolean>

    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun refresh()
}