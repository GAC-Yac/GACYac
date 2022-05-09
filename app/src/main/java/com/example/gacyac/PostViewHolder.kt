package com.example.gacyac

import android.content.ContentValues
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.PostItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import layout.ButtonHighlighterOnTouchListener2

class PostViewHolder(val postBinding: PostItemBinding):RecyclerView.ViewHolder(postBinding.root) {
    private var database = Firebase.firestore


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
            saveToDatabase(bpts)
        }

        capButton.setOnClickListener(){
            Log.d("CAP_BUTTON_POST", "$postBinding")
            var bpts = Integer.parseInt(post.bonuspoints.toString())
            bpts--
            post.bonuspoints = (bpts)
            postBinding.bpPlaceholder.text = post.bonuspoints.toString()
            Log.d("POST_BONUSPOINTS", post.bonuspoints.toString())
            saveToDatabase(bpts)
        }
    }

    private fun saveToDatabase(newBonusPoints: Int) {
        Log.d("timeDEBUGLUKE", postBinding.tcPlaceholder.text.toString())
        val updating = database.collection("newererPosts").document(postBinding.tcPlaceholder.text.toString())
        updating.update("bonuspoints", newBonusPoints).addOnSuccessListener {
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