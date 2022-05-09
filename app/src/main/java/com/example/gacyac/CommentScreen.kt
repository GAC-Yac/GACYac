package com.example.gacyac

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentScreen: AppCompatActivity() {
    // to be gathered
    private lateinit var postID : String
    private lateinit var commentID: String
    private lateinit var backButton: ImageButton
    private lateinit var commentList : ArrayList<Comment>
    var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comments)

        var toolbar = findViewById<Toolbar>(R.id.commentsToolbar)
        setSupportActionBar(toolbar)
        displayComments(postID, commentList)

        backButton = findViewById(R.id.back_button_user)
        backButton.setOnClickListener{
            finish()
        }



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