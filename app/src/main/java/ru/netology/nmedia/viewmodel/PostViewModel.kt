package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositoryRoomImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao()
    )

    val data: LiveData<List<Post>> = repository.data
    val edited = MutableLiveData(empty)

    companion object {
        private val empty = Post(
            id = 0,
            author = "",
            content = "",
            published = "",
        )
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun save() {
        edited.value?.let {
            if (it.content.isBlank()) return
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        edited.value = edited.value?.copy(content = content.trim())
    }
}