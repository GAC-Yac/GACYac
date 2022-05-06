package com.example.gacyac

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Date

//val maxPostID = 0

class CreateComment : AppCompatActivity() {
    private lateinit var sendButton: Button
    private lateinit var commentText: EditText
    private lateinit var documentID: String
    private lateinit var userID: TextView
    private lateinit var sendingText: EditText

    var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_comment)

        sendButton = findViewById(R.id.send)
        sendingText = findViewById(R.id.commentText)

        commentText = findViewById(R.id.commentT)
        userID = findViewById(R.id.commentU)
        val database = Firebase.firestore

        val rootRef = FirebaseFirestore.getInstance()
        val productsRef = rootRef.collection("newerPosts")
        var count = 0
        productsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let {
                    for (snapshot in it) {
                        count++
                    }
                }
                print("count: $count")
            } else {
                task.exception?.message?.let {
                    print(it)
                }
            }
        }

        fun saveToDatabase() {
            val comment = commentText.getText().toString()
            val timePostCreated = Date(System.currentTimeMillis())
            val currentPostingID = database.collection("admin").document("postIDs")
            currentPostingID.get().toString()

            //database.collection("admin").document("postIDs").get()
            //val tempVar = ""


            val newComment = hashMapOf(
                "comment" to comment,
                "user" to userID,
                "time" to timePostCreated
            )

            database.collection("newerPosts")
                .add(newComment)
                .addOnSuccessListener { documentReference ->
                    documentID = documentReference.id
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }

        }

        sendButton.setOnClickListener {
            saveToDatabase()
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, CreatePost::class.java)
        }
    }




}