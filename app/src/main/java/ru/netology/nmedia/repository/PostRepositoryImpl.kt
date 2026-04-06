package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl : PostRepository {

    override fun getAll(callback: PostRepository.Callback<List<Post>>) {
        PostApi.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException("HTTP ${response.code()}"))
                    return
                }
                callback.onSuccess(response.body() ?: emptyList())
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
        val call = if (post.id == 0L) PostApi.save(post) else PostApi.update(post.id, post)
        call.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException("HTTP ${response.code()}"))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        PostApi.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException("HTTP ${response.code()}"))
                    return
                }
                callback.onSuccess(Unit)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

    override fun likeById(id: Long, callback: PostRepository.Callback<Post>) {
        PostApi.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException("HTTP ${response.code()}"))
                    return
                }
                val post = response.body()?.find { it.id == id }
                    ?: run { callback.onError(RuntimeException("Post not found")); return }
                val likeCall = if (post.likedByMe) PostApi.unlikeById(id) else PostApi.likeById(id)
                likeCall.enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException("HTTP ${response.code()}"))
                            return
                        }
                        callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(Exception(t))
                    }
                })
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }
}
