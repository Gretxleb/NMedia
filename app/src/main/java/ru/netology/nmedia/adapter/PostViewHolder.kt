package ru.netology.nmedia.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            content.text = post.content
            published.text = post.published
            like.isChecked = post.likedByMe
            like.text = formatCount(post.likes)
            share.text = formatCount(post.shares)

            like.setOnClickListener { onInteractionListener.onLike(post) }
            share.setOnClickListener { onInteractionListener.onShare(post) }

            menu.setOnClickListener {
                it.showContextMenu()
            }

            itemView.setOnCreateContextMenuListener { menu, _, _ ->
                menu.add(0, 0, 0, ru.netology.nmedia.R.string.edit_post)
                    .setOnMenuItemClickListener {
                        onInteractionListener.onEdit(post)
                        true
                    }
                menu.add(0, 1, 1, ru.netology.nmedia.R.string.remove_post)
                    .setOnMenuItemClickListener {
                        onInteractionListener.onRemove(post)
                        true
                    }
            }
        }
    }

    private fun formatCount(count: Int): String = when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 10_000 -> "${count / 1000}K"
        count >= 1_100 -> String.format("%.1fK", count / 1000.0)
        else -> count.toString()
    }
}