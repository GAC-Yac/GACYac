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
import java.sql.Date
import java.text.SimpleDateFormat

class PostViewHolder(val postBinding: PostItemBinding):RecyclerView.ViewHolder(postBinding.root) {
    private var database = Firebase.firestore


    fun bindPost(post: Post){

        postBinding.postTitle.text = post.title
        postBinding.postText.text = post.text
        postBinding.postCreator.text = post.username
        postBinding.tcPlaceholder.text = post.time.toString()
        postBinding.bpPlaceholder.text = post.bonuspoints.toString()
        postBinding.postCreator.text = post.userID
        postBinding.postID.text = post.postID
        postBinding.androidIdentifierPost.text = post.androidID.toString()

        val faxButton: ImageButton = postBinding.faxPlaceholder
        faxButton.setOnTouchListener(ButtonHighlighterOnTouchListener2(faxButton))

        val capButton: ImageButton = postBinding.capPlaceholder
        capButton.setOnTouchListener(ButtonHighlighterOnTouchListener2(capButton))

        // upvote button, integrated with total bonus points score
        faxButton.setOnClickListener(){
            Log.d("FAX_BUTTON_POST", "$postBinding")
            var bpts = Integer.parseInt(post.bonuspoints.toString())
            bpts++
            post.bonuspoints = (bpts)
            postBinding.bpPlaceholder.text = post.bonuspoints.toString()
            Log.d("POST_BONUSPOINTS", post.bonuspoints.toString())
            saveToDatabase(bpts)
            addKarma(1, post.androidID.toString()) // updates users bp
        }

        // downvote button, integrated with total bonus points score
        capButton.setOnClickListener(){
            Log.d("CAP_BUTTON_POST", "$postBinding")
            var bpts = Integer.parseInt(post.bonuspoints.toString())
            bpts--
            post.bonuspoints = (bpts)
            postBinding.bpPlaceholder.text = post.bonuspoints.toString()
            Log.d("POST_BONUSPOINTS", post.bonuspoints.toString())
            saveToDatabase(bpts)
            Log.d("POST_BONUSPOINTS", post.androidID.toString())
            addKarma(-1, post.androidID.toString()) // updates users bp
        }
    }



    // increments total user karma by scale of 1 based on post details
    private fun addKarma(addedKarma: Long, userID1: String) {
        Log.d("amberDONKEY", "$userID1")
        database.collection("users").document(userID1).get()
            .addOnSuccessListener { documentReference ->

                var bp = documentReference.get("bonuspoints") as Long
                var bd : Long
                if (addedKarma > 0) {
                    bd = bp.inc()
                } else {
                    bd = bp.dec()
                }

                Log.d("bonusPointsOfUser", "$bd")
                changeUserKarma(bd, userID1)

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Could not Log In", e)
            }
    }

    // updates karma in database, reference call in addKarma
    private fun changeUserKarma(newKarma: Long, userID: String) {
        val updating = database.collection("users").document(userID)
        updating.update("bonuspoints", newKarma).addOnSuccessListener {
            Log.d("found_referenced_post", "success")
        }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating document", e)
            }
    }


    // updates bonus point value for each post
    private fun saveToDatabase(newBonusPoints: Int) {
        //Log.d("timeDEBUGLUKE", postBinding.postID.text.toString())
        val updating = database.collection("newererPosts").document(postBinding.postID.text.toString())
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