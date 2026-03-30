package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl(
    private val client: OkHttpClient = OkHttpClient(),
    private val gson: Gson = Gson(),
) : PostRepository {
    private val listType = object : TypeToken<List<Post>>() {}.type

    private fun url(path: String): String = "http://10.0.2.2:9999$path"

    override fun getAll(callback: PostRepository.Callback<List<Post>>) {
        val request = Request.Builder()
            .get()
            .url(url("/api/posts"))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                response.body?.use { body ->
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("HTTP ${response.code}"))
                        return
                    }
                    val json = body.string()
                    val posts: List<Post> = gson.fromJson(json, listType)
                    callback.onSuccess(posts)
                } ?: run {
                    callback.onError(RuntimeException("Empty body"))
                }
            }
        })
    }

    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
        val isNew = post.id == 0L
        val method = if (isNew) "POST" else "PUT"
        val requestUrl = if (isNew) url("/api/posts") else url("/api/posts/${post.id}")

        val json = gson.toJson(post)
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .method(method, requestBody)
            .url(requestUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                response.body?.use { body ->
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("HTTP ${response.code}"))
                        return
                    }
                    val jsonResponse = body.string()
                    val saved = gson.fromJson(jsonResponse, Post::class.java)
                    callback.onSuccess(saved)
                } ?: run {
                    callback.onError(RuntimeException("Empty body"))
                }
            }
        })
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        val request = Request.Builder()
            .delete()
            .url(url("/api/posts/$id"))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(RuntimeException("HTTP ${response.code}"))
                }
            }
        })
    }

    override fun likeById(id: Long, callback: PostRepository.Callback<Post>) {
        val getRequest = Request.Builder()
            .get()
            .url(url("/api/posts/$id"))
            .build()

        client.newCall(getRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException("HTTP ${response.code}"))
                    return
                }

                val body = response.body
                if (body == null) {
                    callback.onError(RuntimeException("Empty body"))
                    return
                }

                body.use {
                    val jsonResponse = it.string()
                    val post: Post = gson.fromJson(jsonResponse, Post::class.java)

                    val likeRequest = if (!post.likedByMe) {
                        Request.Builder()
                            .post("{}".toRequestBody("application/json; charset=utf-8".toMediaType()))
                            .url(url("/api/posts/$id/likes"))
                            .build()
                    } else {
                        Request.Builder()
                            .delete()
                            .url(url("/api/posts/$id/likes"))
                            .build()
                    }

                    client.newCall(likeRequest).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: java.io.IOException) {
                            callback.onError(e)
                        }

                        override fun onResponse(call: Call, likeResponse: okhttp3.Response) {
                            likeResponse.body?.use { likeBody ->
                                if (!likeResponse.isSuccessful) {
                                    callback.onError(RuntimeException("HTTP ${likeResponse.code}"))
                                    return
                                }
                                val jsonLike = likeBody.string()
                                val updated = gson.fromJson(jsonLike, Post::class.java)
                                callback.onSuccess(updated)
                            } ?: run {
                                callback.onError(RuntimeException("Empty body"))
                            }
                        }
                    })
                }
            }
        })
    }
}
