package ru.netology.nmedia.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.model.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int,
    val shares: Int
) {
    fun toDto() = Post(
        id = id,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
        shares = shares
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            id = dto.id,
            author = dto.author,
            content = dto.content,
            published = dto.published,
            likedByMe = dto.likedByMe,
            likes = dto.likes,
            shares = dto.shares
        )
    }
}