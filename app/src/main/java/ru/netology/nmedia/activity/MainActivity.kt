package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewmodels.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.formatCount
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        viewModel.data.observe(this) { post ->
            with(binding) {
                authorName.text = post.author
                postText.text = post.content
                postDate.text = post.published
                likeCount.text = formatCount(post.likes)
                commentCount.text = formatCount(post.shares)

                likeButton.setImageResource(
                    if (post.likedByMe) android.R.drawable.btn_star_big_on 
                    else android.R.drawable.btn_star_big_off
                )
            }
        }

        binding.likeButton.setOnClickListener {
            viewModel.like()
        }

        binding.shareButton.setOnClickListener {
            viewModel.share()
        }
    }
}