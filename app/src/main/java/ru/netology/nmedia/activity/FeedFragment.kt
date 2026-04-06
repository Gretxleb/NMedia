package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()
    private var binding: FragmentFeedBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFeedBinding.bind(view)

        val adapter = PostsAdapter(
            onLike = { viewModel.likeById(it.id) },
            onRemove = { viewModel.removeById(it.id) },
            onEdit = {},
            onPostClick = {
                val action = FeedFragmentDirections.actionFeedFragmentToPostDetailsFragment(it.id)
                findNavController().navigate(action)
            },
            onVideoClick = {}
        )

        binding?.list?.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { feedModel ->
            adapter.submitList(feedModel.posts)
            binding?.swipeRefresh?.isRefreshing = feedModel.loading
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.loadPosts()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}