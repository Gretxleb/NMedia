fun bind(post: Post) {
    binding.apply {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        
        // Для MaterialButton используем свойство isChecked и текст
        like.isChecked = post.likedByMe
        like.text = formatCount(post.likes) // Используй свою функцию сокращения чисел (1K, 1.1M)
        share.text = formatCount(post.shares)

        like.setOnClickListener {
            onInteractionListener.onLike(post)
        }
        share.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        // ... остальной код (меню)
    }
}

// Вспомогательная функция (можно вынести в отдельный файл или оставить тут)
fun formatCount(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 1100 -> "1K"
        count < 10000 -> String.format("%.1fK", count / 1000.0)
        count < 1000000 -> (count / 1000).toString() + "K"
        else -> String.format("%.1fM", count / 1000000.0)
    }
}