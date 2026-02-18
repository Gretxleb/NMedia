package ru.netology.nmedia.dto

enum class Action {
    LIKE,
    POST,
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
)

data class NewPost(
    val userName: String,
    val postText: String,
)