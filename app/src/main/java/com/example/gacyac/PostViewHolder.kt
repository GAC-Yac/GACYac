package com.example.gacyac

import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.PostItemBinding

class PostViewHolder(private val postBinding: PostItemBinding):RecyclerView.ViewHolder(postBinding.root) {
    fun bindPost(post: Post){
        postBinding.postTitle.text = post.title
        postBinding.postText.text = post.text
        postBinding.postCreator.text = post.username
        postBinding.tcPlaceholder.text = post.time.toString()
        postBinding.bpPlaceholder.text = post.bonuspoints.toString()
        //postBinding.bpPlaceholder.text = post.id
    }
}