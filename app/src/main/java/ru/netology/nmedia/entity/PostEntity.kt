package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: Int = 0,
    val hidden: Boolean = false
) {
    fun toDto() = Post(id, author, authorAvatar, content, published, likedByMe, likes, shares)

    companion object {
        fun fromDto(dto: Post, hidden: Boolean = false) = PostEntity(
            dto.id,
            dto.author,
            dto.authorAvatar,
            dto.content,
            dto.published,
            dto.likedByMe,
            dto.likes,
            dto.shares,
            hidden
        )
    }
}
