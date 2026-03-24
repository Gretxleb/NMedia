package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.model.Post

class PostsAdapter(
    private val onShare: (Post) -> Unit,
    private val onRemove: (Post) -> Unit,
    private val onEdit: (Post) -> Unit,
    private val onPostClick: (Post) -> Unit,
    private val onVideoClick: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {

    var onLikeListener: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(
        private val binding: CardPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.author.text = post.author
            binding.published.text = post.published
            binding.content.text = post.content
            binding.like.text = post.likes.toString()
            binding.share.text = post.shares.toString()
            binding.like.isChecked = post.likedByMe

            binding.videoGroup.visibility = if (post.video.isNullOrBlank()) View.GONE else View.VISIBLE

            binding.like.setOnClickListener {
                onLikeListener?.invoke(post)
            }

            binding.share.setOnClickListener {
                onShare(post)
            }

            binding.menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            binding.playVideo.setOnClickListener {
                onVideoClick(post)
            }

            binding.videoPreview.setOnClickListener {
                onVideoClick(post)
            }

            binding.root.setOnClickListener {
                onPostClick(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem
}
