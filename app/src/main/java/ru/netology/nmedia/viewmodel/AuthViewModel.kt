package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.model.AuthModelState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
    private val apiService: PostApiService
) : ViewModel() {
    private val _data = MutableLiveData(AuthModelState())
    val data: LiveData<AuthModelState> = _data

    fun login(login: String, pass: String) = viewModelScope.launch {
        _data.value = AuthModelState(loading = true)
        try {
            val response = apiService.updateUser(login, pass)
            if (!response.isSuccessful) {
                _data.value = AuthModelState(error = true)
                return@launch
            }
            val token = response.body() ?: throw Exception()
            appAuth.setAuth(token.id, token.token)
            _data.value = AuthModelState(success = true)
        } catch (e: Exception) {
            _data.value = AuthModelState(error = true)
        }
    }

    fun register(login: String, pass: String, name: String) = viewModelScope.launch {
        _data.value = AuthModelState(loading = true)
        try {
            val response = apiService.registerUser(login, pass, name)
            if (!response.isSuccessful) {
                _data.value = AuthModelState(error = true)
                return@launch
            }
            val token = response.body() ?: throw Exception()
            appAuth.setAuth(token.id, token.token)
            _data.value = AuthModelState(success = true)
        } catch (e: Exception) {
            _data.value = AuthModelState(error = true)
        }
    }
}