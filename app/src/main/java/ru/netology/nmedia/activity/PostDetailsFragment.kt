package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class PostDetailsFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels()
    private val args: PostDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == args.postId } ?: return@observe
            binding.author.text = post.author
            binding.published.text = post.published
            binding.content.text = post.content
        }

        return binding.root
    }
}