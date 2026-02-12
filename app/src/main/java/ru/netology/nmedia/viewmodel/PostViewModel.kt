package ru.netology.nmedia.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "Me",
    likedByMe = false,
    published = "Now"
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao()
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    private val prefs = application.getSharedPreferences("draft", Context.MODE_PRIVATE)
    private val draftKey = "draft_content"

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
        saveDraft(null)
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun getDraft(): String? = prefs.getString(draftKey, null)

    fun saveDraft(content: String?) {
        prefs.edit().putString(draftKey, content).apply()
    }
}