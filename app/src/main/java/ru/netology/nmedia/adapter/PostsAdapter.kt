package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostsAdapter(
    private val onLike: (Post) -> Unit,
    private val onRemove: (Post) -> Unit,
    private val onEdit: (Post) -> Unit,
    private val onPostClick: (Post) -> Unit,
    private val onVideoClick: (String) -> Unit
) : ListAdapter<Post, PostsAdapter.ViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: CardPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.author.text = post.author
            binding.published.text = post.published.toString()
            binding.content.text = post.content
            binding.like.isChecked = post.likedByMe
            binding.like.text = post.likes.toString()

            Glide.with(binding.avatar)
                .load("http://10.0.2.2:9999/avatars/${post.authorAvatar}")
                .placeholder(android.R.drawable.ic_menu_gallery)
                .transform(CircleCrop())
                .into(binding.avatar)

            binding.root.setOnClickListener { onPostClick(post) }
            binding.like.setOnClickListener { onLike(post) }
            binding.menu.setOnClickListener { onRemove(post) }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem
}