package com.example.gacyac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.PostItemBinding

class PostAdapter(private val posts: List<Post>): RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = PostItemBinding.inflate(from, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindPost(posts[position])
    }

    override fun getItemCount(): Int = posts.size

}