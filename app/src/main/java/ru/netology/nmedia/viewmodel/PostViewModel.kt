package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    author = "",
    authorAvatar = "",
    content = "",
    likedByMe = false,
    likes = 0,
    published = 0L,
    shares = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(application).postDao()
    )

    val data = repository.data.asLiveData()
    val newerCount = repository.newerCount.asLiveData()

    private val _state = MutableLiveData(FeedModelState())
    val state: LiveData<FeedModelState> = _state

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Post> get() = _edited

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit> get() = _postCreated

    init {
        loadPosts()
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                delay(10_000L)
                try {
                    repository.getNewer()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadPosts() {
        viewModelScope.launch {
            _state.value = FeedModelState(loading = true)
            try {
                repository.getAll()
                _state.value = FeedModelState()
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }
        }
    }

    fun showNewPosts() {
        viewModelScope.launch {
            repository.showAll()
        }
    }

    fun save() {
        val post = _edited.value ?: return
        viewModelScope.launch {
            try {
                repository.save(post)
                _postCreated.value = Unit
                _edited.value = empty
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }
        }
    }

    fun edit(post: Post) {
        _edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (_edited.value?.content == text) return
        _edited.value = _edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.likeById(id)
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }
        }
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }
        }
    }
}
}
