package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Подключаем ViewBinding (генерация класса на основе activity_main.xml)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализируем данные поста
        var post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет! Это новая Нетология. Наша миссия — помочь каждому найти свой путь в мире профессий. Вы учитесь — мы помогаем!",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 1099995
        )

        // Заполняем View данными из объекта post
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesText.text = formatValue(post.likes)
            shareText.text = formatValue(post.shares)

            // Устанавливаем иконку в зависимости от того, нажат ли лайк
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )

            // Обработка клика по кнопке Like
            like.setOnClickListener {
                // В реальном приложении это будет делать ViewModel, 
                // но для текущего задания меняем состояние прямо здесь
                val newLikedByMe = !post.likedByMe
                val newLikes = if (newLikedByMe) post.likes + 1 else post.likes - 1
                
                post = post.copy(likedByMe = newLikedByMe, likes = newLikes)
                
                likesText.text = formatValue(post.likes)
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
            }

            // Обработка клика по кнопке Share
            share.setOnClickListener {
                post = post.copy(shares = post.shares + 1)
                shareText.text = formatValue(post.shares)
            }
        }
    }

    // Функция форматирования чисел (напр. 1100 -> 1.1K)
    private fun formatValue(value: Int): String {
        return when {
            value < 1000 -> value.toString()
            value < 10000 -> {
                val formatted = String.format("%.1f", value / 1000.0)
                if (formatted.endsWith(".0")) formatted.substringBefore(".") + "K" else formatted + "K"
            }
            value < 1000000 -> "${value / 1000}K"
            else -> {
                val formatted = String.format("%.1f", value / 1000000.0)
                if (formatted.endsWith(".0")) formatted.substringBefore(".") + "M" else formatted + "M"
            }
        }
    }
}