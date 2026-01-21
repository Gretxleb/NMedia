package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.CountFormatter
import ru.netology.nmedia.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет! Это новая Нетология...",
            published = "21 мая в 18:36",
            likedByMe = false,
            likesCount = 999,
            shareCount = 995
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            
            // Первичная отрисовка чисел
            likesText.text = CountFormatter.format(post.likesCount)
            shareText.text = CountFormatter.format(post.shareCount)

            like.setOnClickListener {
                post = post.copy(likedByMe = !post.likedByMe)
                post = if (post.likedByMe) {
                    post.copy(likesCount = post.likesCount + 1)
                } else {
                    post.copy(likesCount = post.likesCount - 1)
                }
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                likesText.text = CountFormatter.format(post.likesCount)
            }

            share.setOnClickListener {
                post = post.copy(shareCount = post.shareCount + 1)
                shareText.text = CountFormatter.format(post.shareCount)
            }
        }
    }
}