package com.example.gacyac

import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.CommentItemBinding
import com.example.gacyac.databinding.PostItemBinding

class CommentViewHolder(private val commentBinding: CommentItemBinding):RecyclerView.ViewHolder(commentBinding.root) {
    fun bindPost(comment: Comment){
        commentBinding.commentT.text = comment.commentText
        commentBinding.commentU.text = comment.user
        commentBinding.commentTime.text = comment.time.toString()
    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
        }
        return this
    }
}