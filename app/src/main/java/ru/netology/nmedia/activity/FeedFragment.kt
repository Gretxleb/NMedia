package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private lateinit var viewModel: PostViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(PostViewModel::class.java)

        val adapter = PostsAdapter(
            onLike = { viewModel.likeById(it.id) },
            onRemove = { viewModel.removeById(it.id) },
            onEdit = { viewModel.edit(it) },
            onPostClick = {},
            onVideoClick = {},
            onImageClick = { url ->
                val bundle = Bundle().apply {
                    putString("url", url)
                }
                findNavController().navigate(
                    ru.netology.nmedia.R.id.action_feedFragment_to_imageFragment,
                    bundle
                )
            }
        )

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}