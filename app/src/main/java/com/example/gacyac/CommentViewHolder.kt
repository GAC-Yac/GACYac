package com.example.gacyac

import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.CommentItemBinding

class CommentViewHolder(private val commentBinding: CommentItemBinding):RecyclerView.ViewHolder(commentBinding.root) {
    fun bindComment(comment: Comment){
        commentBinding.commentText.text = comment.text
        commentBinding.commentCreator.text = comment.userID
    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
        }
        return this
    }
}