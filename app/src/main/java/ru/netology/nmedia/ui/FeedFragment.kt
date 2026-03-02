package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = PostsAdapter(
            onLike = { viewModel.likeById(it.id) },
            onShare = { viewModel.shareById(it.id) },
            onRemove = { viewModel.removeById(it.id) },
            onEdit = { viewModel.edit(it) },
            onPostClick = {
                val action = FeedFragmentDirections.actionFeedFragmentToPostDetailsFragment(it.id)
                findNavController().navigate(action)
            }
        )

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.emptyText.isVisible = it.isEmpty()
        }

        binding.add.setOnClickListener {
            findNavController().navigate(
                FeedFragmentDirections.actionFeedFragmentToNewPostFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}