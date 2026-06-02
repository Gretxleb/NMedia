package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent
import javax.inject.Inject

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

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val appAuth: AppAuth
) : ViewModel() {

    val data = repository.data.asLiveData()
    val newerCount = repository.newerCount.asLiveData()

    private val _state = MutableLiveData(FeedState())
    val state: LiveData<FeedState> = _state

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Post> get() = _edited

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit> get() = _postCreated

    private val _signInRequired = SingleLiveEvent<Unit>()
    val signInRequired: LiveData<Unit> get() = _signInRequired

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
            _state.value = FeedState(loading = true)
            try {
                repository.getAll()
                _state.value = FeedState()
            } catch (e: Exception) {
                _state.value = FeedState(error = true)
            }
        }
    }

    fun showNewPosts() {
        viewModelScope.launch {
            repository.showAll()
        }
    }

    fun save() {
        if (appAuth.authState.value == null) {
            _signInRequired.value = Unit
            return
        }
        val post = _edited.value ?: return
        viewModelScope.launch {
            try {
                repository.save(post)
                _postCreated.value = Unit
                _edited.value = empty
            } catch (e: Exception) {
                _state.value = FeedState(error = true)
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
        if (appAuth.authState.value == null) {
            _signInRequired.value = Unit
            return
        }
        viewModelScope.launch {
            try {
                repository.likeById(id)
            } catch (e: Exception) {
                _state.value = FeedState(error = true)
            }
        }
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
            } catch (e: Exception) {
                _state.value = FeedState(error = true)
            }
        }
    }

    fun isAuthenticated(): Boolean {
        return appAuth.authState.value != null
    }
}
