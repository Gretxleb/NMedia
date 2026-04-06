package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()
    private var binding: FragmentFeedBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFeedBinding.bind(view)

        val adapter = PostAdapter(
            onLike = { viewModel.likeById(it.id) },
            onRemove = { viewModel.removeById(it.id) },
            onEdit = {},
            onPostClick = {
                val action = FeedFragmentDirections.actionFeedFragmentToPostFragment(it.id)
                findNavController().navigate(action)
            },
            onVideoClick = {}
        )

        binding?.list?.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding?.swipeRefresh?.isRefreshing = it
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}