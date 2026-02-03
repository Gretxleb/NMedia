package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewmodels.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { text ->
            text?.let {
                viewModel.changeContent(it)
                viewModel.save()
            }
        }

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) = viewModel.likeById(post.id)
            override fun onRemove(post: Post) = viewModel.removeById(post.id)
            override fun onShare(post: Post) = viewModel.shareById(post.id)

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                newPostLauncher.launch(post.content)
            }

            override fun onVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch(null)
        }
    }
}