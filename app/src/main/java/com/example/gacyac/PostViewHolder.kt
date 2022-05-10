package com.example.gacyac

import android.content.ContentValues
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.PostItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import layout.ButtonHighlighterOnTouchListener2

class PostViewHolder(private val postBinding: PostItemBinding):RecyclerView.ViewHolder(postBinding.root) {
    private var database = Firebase.firestore

    fun bindPost(post: Post){
        postBinding.postTitle.text = post.title
        postBinding.postText.text = post.text
        postBinding.postCreator.text = post.username
        postBinding.tcPlaceholder.text = post.time.toString()
        postBinding.bpPlaceholder.text = post.bonuspoints.toString()
        postBinding.postCreator.text = post.userID
        postBinding.postID.text = post.postID.toString()

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
            saveToDatabase(bpts)
            //addKarma(1)
        }

        capButton.setOnClickListener(){
            Log.d("CAP_BUTTON_POST", "$postBinding")
            var bpts = Integer.parseInt(post.bonuspoints.toString())
            bpts--
            post.bonuspoints = (bpts)
            postBinding.bpPlaceholder.text = post.bonuspoints.toString()
            Log.d("POST_BONUSPOINTS", post.bonuspoints.toString())
            saveToDatabase(bpts)
            //Log.d("POST_BONUSPOINTS", post.androidID.toString())
            //addKarma(-1, post.androidID.toString())
        }
    }

    private fun saveToDatabase(newBonusPoints: Int) {
        Log.d("NIKITApostID", postBinding.postID.text.toString())
        val testing = postBinding.postID.text.toString()
        database.collection("newererPosts").document(testing)
        .update("bonuspoints", newBonusPoints).addOnSuccessListener {
            Log.d("found_referenced_post", "post bonus points changed to $newBonusPoints")
        }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating document", e)
            }
    }


    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
        }
        return this
    }
}