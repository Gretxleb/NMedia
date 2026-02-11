package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        adapter = PostAdapter()
        binding.recyclerView.adapter = adapter
        
        val posts = listOf(
            Post(1, "Автор 1", "Привет, мир!", "5 мин назад", 10, 3),
            Post(2, "Автор 2", "Второй пост", "10 мин назад", 5, 1)
        )
        
        adapter.submitList(posts)
    }
}