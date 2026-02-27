package ru.netology.nmedia.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.model.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int,
    val shares: Int,
    val video: String?
) {
    fun toModel(): Post {
        return Post(
            id = id,
            author = author,
            content = content,
            published = published,
            likedByMe = likedByMe,
            likes = likes,
            shares = shares,
            video = video
        )
    }

    companion object {
        fun fromModel(post: Post): PostEntity {
            return PostEntity(
                id = post.id,
                author = post.author,
                content = post.content,
                published = post.published,
                likedByMe = post.likedByMe,
                likes = post.likes,
                shares = post.shares,
                video = post.video
            )
        }
    }
}