package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = "",
    shares = 0,
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryFileImpl(application)

    val data: LiveData<List<Post>> = repository.data

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Post> = _edited

    fun likeById(id: Long) {
        repository.likeById(id)
    }

    fun shareById(id: Long) {
        repository.shareById(id)
    }

    fun removeById(id: Long) {
        repository.removeById(id)
    }

    fun edit(post: Post) {
        _edited.value = post
    }

    fun cancelEdit() {
        _edited.value = empty
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (_edited.value?.content == text) return
        _edited.value = _edited.value?.copy(content = text)
    }

    fun save() {
        _edited.value?.let {
            repository.save(it)
        }
        _edited.value = empty
    }
}