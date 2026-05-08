package ru.netology.nmedia.auth

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.netology.nmedia.dto.Token

class AppAuth private constructor(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val _authState = MutableLiveData<Token?>()
    val authState: LiveData<Token?> = _authState
    val authId: Long?
        get() = _authState.value?.id

    init {
        val tokenJson = prefs.getString(TOKEN_KEY, null)
        if (tokenJson != null) {
            try {
                _authState.value = gson.fromJson(tokenJson, Token::class.java)
            } catch (e: JsonSyntaxException) {
                prefs.edit { clear() }
            }
        }
    }

    @Synchronized
    fun setAuth(id: Long, token: String) {
        val authToken = Token(id, token)
        _authState.value = authToken
        prefs.edit {
            putString(TOKEN_KEY, gson.toJson(authToken))
        }
    }

    @Synchronized
    fun removeAuth() {
        _authState.value = null
        prefs.edit { clear() }
    }

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val ID_KEY = "ID_KEY"

        @Volatile
        private var instance: AppAuth? = null

        fun getInstance(): AppAuth = synchronized(this) {
            instance ?: throw IllegalStateException("AppAuth is not initialized")
        }

        fun initAppAuth(context: Context): AppAuth = instance ?: synchronized(this) {
            instance ?: AppAuth(context).also { instance = it }
        }
    }
}