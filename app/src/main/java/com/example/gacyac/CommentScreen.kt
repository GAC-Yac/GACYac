package com.example.gacyac

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentScreen: AppCompatActivity() {
    // to be gathered
    private lateinit var postID : String
    private lateinit var commentID: String
    private lateinit var commentList : ArrayList<Comment>
    var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post)

        displayComments(postID, commentList)

    }

    private fun displayComments(postID: String, commentList: ArrayList<Comment>) {
        database = FirebaseFirestore.getInstance()
        database.collection("newererPosts").document(postID).collection("comments").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("ERROR", error.message.toString())
                    return
                }

            }

        })
        //getProfileInformation(device_id)
        //getLeaderboardInformation()
    }
}