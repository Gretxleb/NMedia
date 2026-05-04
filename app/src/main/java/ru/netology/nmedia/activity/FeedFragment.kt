
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels()
    private var binding: FragmentFeedBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val b = FragmentFeedBinding.inflate(inflater, container, false)
        binding = b
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = binding ?: return

        val adapter = PostsAdapter(
            onLike = { viewModel.likeById(it.id) },
            onRemove = { viewModel.removeById(it.id) },
            onEdit = { viewModel.edit(it) },
            onPostClick = {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postDetailsFragment,
                    Bundle().apply { putLong("postId", it.id) }
                )
            },
            onVideoClick = {},
            onImageClick = {}
        )

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
            binding.emptyText.isVisible = posts.isEmpty()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swipeRefresh.isRefreshing = state.loading
            binding.errorGroup.isVisible = state.error
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadPosts()
        }

        viewModel.signInRequired.observe(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setTitle("Sign In Required")
                .setMessage("You need to sign in to perform this action.")
                .setPositiveButton("Sign In") { _, _ ->
                    findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.fab.setOnClickListener {
            if (viewModel.isAuthenticated()) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Sign In Required")
                    .setMessage("You need to sign in to create a post.")
                    .setPositiveButton("Sign In") { _, _ ->
                        findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
