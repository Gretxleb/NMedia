package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl
import ru.netology.nmedia.util.SingleLiveEvent

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
    private val repository: PostRepository = PostRepositoryFileImpl(application)

    val data: LiveData<FeedModel> = repository.data.map {
        FeedModel(posts = it, empty = it.isEmpty())
    }

    private val _state = MutableLiveData<FeedModel>()
    val state: LiveData<FeedModel>
        get() = _state

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Post>
        get() = _edited

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _state.value = FeedModel(loading = true)
            repository.getAll()
            _state.value = FeedModel()
        } catch (e: Exception) {
            _state.value = FeedModel(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _state.value = FeedModel(refreshing = true)
            repository.getAll()
            _state.value = FeedModel()
        } catch (e: Exception) {
            _state.value = FeedModel(error = true)
        }
    }

    fun save() {
        _edited.value?.let {
            viewModelScope.launch {
                try {
                    repository.save(it)
                    _postCreated.value = Unit
                } catch (e: Exception) {
                    _state.value = FeedModel(error = true)
                }
            }
        }
        _edited.value = empty
    }

    fun edit(post: Post) {
        _edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (_edited.value?.content == text) {
            return
        }
        _edited.value = _edited.value?.copy(content = text)
    }

    fun likeById(id: Long) = viewModelScope.launch {
        try {
            repository.likeById(id)
        } catch (e: Exception) {
            _state.value = FeedModel(error = true)
        }
    }

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            repository.removeById(id)
        } catch (e: Exception) {
            _state.value = FeedModel(error = true)
        }
    }

    fun shareById(id: Long) = viewModelScope.launch {
        repository.shareById(id)
    }
}
