package com.example.gacyac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.CommentItemBinding
import com.example.gacyac.databinding.PostItemBinding

class CommentAdapter(private val comments: List<Comment>): RecyclerView.Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CommentItemBinding.inflate(from, parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bindComment(comments[position])
    }

    override fun getItemCount(): Int = comments.size

}