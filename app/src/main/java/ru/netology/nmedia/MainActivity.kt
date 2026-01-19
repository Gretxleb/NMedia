package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.formatCount

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            id = 1,
            author = "Иван Иванов",
            content = "Это пример текста поста. Нажмите на лайк или репост!",
            published = "15 мая в 18:30",
            likes = 999,
            shares = 995
        )

        fun render(post: Post) {
            binding.authorName.text = post.author
            binding.postText.text = post.content
            binding.postDate.text = post.published
            
            binding.likeButton.setImageResource(
                if (post.likedByMe) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
            )
            
            binding.likeCount.text = formatCount(post.likes)
            binding.commentCount.text = formatCount(post.shares)
        }

        render(post)

        binding.likeButton.setOnClickListener {
            post = post.copy(
                likedByMe = !post.likedByMe,
                likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
            )
            render(post)
        }

        binding.shareButton.setOnClickListener {
            post = post.copy(shares = post.shares + 1)
            render(post)
        }

        binding.root.setOnClickListener {
            println("Click: Root")
        }

        binding.postImage.setOnClickListener {
            println("Click: Avatar/Image")
        }

        binding.postText.setOnClickListener {
            println("Click: Text")
        }
    }
}