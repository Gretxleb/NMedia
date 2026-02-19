package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class PostDetailsFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        val postId = arguments?.getLong("postId") ?: return binding.root

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            binding.author.text = post.author
            binding.published.text = post.published
            binding.content.text = post.content
        }

        return binding.root
    }
}