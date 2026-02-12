package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.dto.Post

class PostDetailsFragment : Fragment() {

    companion object {
        var Bundle.idArg: Long by LongArg
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        val postId = arguments?.idArg ?: -1L

        viewModel.data.observe(viewLifecycleOwner) { state ->
            val post = state.posts.find { it.id == postId } ?: run {
                findNavController().navigateUp()
                return@observe
            }

            val viewHolder = PostViewHolder(binding.postLayout, object : ru.netology.nmedia.adapter.OnInteractionListener {
                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.shareById(post.id)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                    findNavController().navigateUp()
                }

                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    findNavController().navigate(
                        R.id.action_postDetailsFragment_to_newPostFragment,
                        Bundle().apply { textArg = post.content }
                    )
                }
            })
            viewHolder.bind(post)
        }

        return binding.root
    }
}