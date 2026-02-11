package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostItemBinding

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    
    private var posts = listOf<Post>()
    
    fun submitList(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }
    
    override fun getItemCount() = posts.size
    
    inner class PostViewHolder(
        private val binding: PostItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(post: Post) {
            binding.author.text = post.author
            binding.published.text = post.published
            binding.content.text = post.content
            binding.likeButton.text = "❤️ ${post.likes}"
            binding.shareButton.text = "↗️ ${post.shares}"
            
            binding.likeButton.setOnClickListener {
                // Обработка лайка
            }
            
            binding.shareButton.setOnClickListener {
                // Обработка репоста
            }
        }
    }
}