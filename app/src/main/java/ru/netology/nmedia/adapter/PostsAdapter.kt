package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.model.Post

class PostsAdapter(
    private val onLike: (Post) -> Unit,
    private val onShare: (Post) -> Unit,
    private val onRemove: (Post) -> Unit,
    private val onEdit: (Post) -> Unit,
    private val onPostClick: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {

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
            binding.likes.text = post.likes.toString()
            binding.shares.text = post.shares.toString()
            binding.like.isChecked = post.likedByMe

            binding.like.setOnClickListener {
                onLike(post)
            }

            binding.share.setOnClickListener {
                onShare(post)
            }

            binding.remove.setOnClickListener {
                onRemove(post)
            }

            binding.edit.setOnClickListener {
                onEdit(post)
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