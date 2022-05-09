package com.example.gacyac

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.PostItemBinding
import layout.ButtonHighlighterOnTouchListener2

class PostViewHolder(private val postBinding: PostItemBinding):RecyclerView.ViewHolder(postBinding.root) {
    fun bindPost(post: Post){

        postBinding.postTitle.text = post.title
        postBinding.postText.text = post.text
        postBinding.postCreator.text = post.username
        postBinding.tcPlaceholder.text = post.time.toString()
        postBinding.bpPlaceholder.text = post.bonuspoints.toString()
        postBinding.postCreator.text = post.userID

        val faxButton: ImageButton = postBinding.faxPlaceholder
        faxButton.setOnTouchListener(ButtonHighlighterOnTouchListener2(faxButton))

        val capButton: ImageButton = postBinding.capPlaceholder
        capButton.setOnTouchListener(ButtonHighlighterOnTouchListener2(capButton))

        faxButton.setOnClickListener(){
            Log.d("FAX_BUTTON_POST", "$postBinding")
            var bpts = Integer.parseInt(post.bonuspoints.toString())
            bpts++
            post.bonuspoints = (bpts)
            postBinding.bpPlaceholder.text = post.bonuspoints.toString()
            Log.d("POST_BONUSPOINTS", post.bonuspoints.toString())
        }

        capButton.setOnClickListener(){
            Log.d("CAP_BUTTON_POST", "$postBinding")
            var bpts = Integer.parseInt(post.bonuspoints.toString())
            bpts--
            post.bonuspoints = (bpts)
            postBinding.bpPlaceholder.text = post.bonuspoints.toString()
            Log.d("POST_BONUSPOINTS", post.bonuspoints.toString())
        }
    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
        }
        return this
    }
}