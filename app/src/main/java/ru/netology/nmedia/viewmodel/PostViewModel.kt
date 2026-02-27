package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryRoomImpl(AppDb.getInstance(application).postDao())

    val data: LiveData<List<Post>> = repository.getAll()

    private val _edited = MutableLiveData<Post?>()
    val edited: MutableLiveData<Post?> = _edited

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

    fun save() {
        val post = _edited.value ?: return
        repository.edit(post)
        _edited.value = null
    }

    fun createPost(content: String) {
        val post = Post(
            id = 0L,
            author = "Me",
            content = content,
            published = "now",
            likedByMe = false,
            likes = 0,
            shares = 0,
            video = null
        )
        repository.save(post)
    }
}