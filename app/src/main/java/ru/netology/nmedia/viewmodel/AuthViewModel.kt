package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.model.AuthModelState

class AuthViewModel : ViewModel() {
    private val _data = MutableLiveData(AuthModelState())
    val data: LiveData<AuthModelState> = _data

    fun login(login: String, pass: String) = viewModelScope.launch {
        _data.value = AuthModelState(loading = true)
        try {
            val response = PostApi.updateUser(login, pass)
            if (!response.isSuccessful) {
                _data.value = AuthModelState(error = true)
                return@launch
            }
            val token = response.body() ?: throw Exception()
            AppAuth.getInstance().setAuth(token.id, token.token)
            _data.value = AuthModelState(success = true)
        } catch (e: Exception) {
            _data.value = AuthModelState(error = true)
        }
    }

    fun register(login: String, pass: String, name: String) = viewModelScope.launch {
        _data.value = AuthModelState(loading = true)
        try {
            val response = PostApi.registerUser(login, pass, name)
            if (!response.isSuccessful) {
                _data.value = AuthModelState(error = true)
                return@launch
            }
            val token = response.body() ?: throw Exception()
            AppAuth.getInstance().setAuth(token.id, token.token)
            _data.value = AuthModelState(success = true)
        } catch (e: Exception) {
            _data.value = AuthModelState(error = true)
        }
    }
}