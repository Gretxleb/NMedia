package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет! Это новая Нетология. Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом есть потенциал, который можно развить.",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999, // Было likesCount
            shares = 995 // Было shareCount
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            
            // Обращение к полям также исправлено на likes и shares
            likesText.text = post.likes.toString()
            shareText.text = post.shares.toString()

            if (post.likedByMe) {
                likes.setImageResource(ru.netology.nmedia.R.drawable.ic_liked_24)
            }

            likes.setOnClickListener {
                // Здесь логика изменения будет в следующих ДЗ, пока просто выводим данные
            }
        }
    }
}