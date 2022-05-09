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
            Log.d("POST_BONUSPOINTS", post.androidID.toString())
            addKarma(-1, post.androidID.toString())
        }
    }





    private fun addKarma(addedKarma: Int, userID1: String) {
        Log.d("amberDONKEY", "$userID1")
        database.collection("users").document(userID1).get()
            .addOnSuccessListener { documentReference ->

                val bp = documentReference.get("bonuspoints") as Int
                changeUserKarma(addedKarma+bp, userID1)

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Could not Log In", e)
            }
    }

    private fun changeUserKarma(newKarma: Int, userID: String) {
        val updating = database.collection("users").document(postBinding.postCreator.text.toString())
        updating.update("bonuspoints", newKarma).addOnSuccessListener {
            Log.d("found_referenced_post", "success")
        }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating document", e)
            }
    }


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