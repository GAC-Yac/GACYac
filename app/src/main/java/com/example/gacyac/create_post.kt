package com.example.gacyac

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CreatePost : AppCompatActivity() {
    private lateinit var saveButton: Button
    private lateinit var postTitleText: EditText
    private lateinit var postBodyText: EditText
    private lateinit var postDateButton: Button
    private lateinit var create: Post
    lateinit var documentID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post)

        saveButton = findViewById(R.id.save_post)
        postTitleText = findViewById(R.id.post_title)
        postBodyText = findViewById(R.id.post_text)
        postDateButton = findViewById(R.id.post_date)
        val database = Firebase.firestore


        /*
        var postTitle = postTitleText.getText().toString()
        var postBody = postBodyText.getText().toString()
        */
        fun saveToDatabase() {
            var postTitle = postTitleText.getText().toString()
            var postBody = postBodyText.getText().toString()
            var timePostCreated = FieldValue.serverTimestamp()

            val newPost = hashMapOf(
                "title" to postTitle,
                "body" to postBody,
                "karma" to 0,
                "created" to timePostCreated
            )

            database.collection("posts")
                .add(newPost)
                .addOnSuccessListener { documentReference ->
                    documentID = documentReference.id
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }

            // Creates a local Post object, for testing
            create = Post(
                postTitle,
                postBody,
                0,
                "Big L",
                timePostCreated,
                "0"
            )
        }

        saveButton.setOnClickListener {
            saveToDatabase()

            // Creates a local post variable
            postList.add(create)

            finish()
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, CreatePost::class.java)
        }
    }




}