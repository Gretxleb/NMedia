package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
        val binding = CardPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: CardPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.root.setOnClickListener { onPostClick(post) }
            binding.like.setOnClickListener { onLike(post) }
            binding.menu.setOnClickListener { onRemove(post) }
        }
    }
}