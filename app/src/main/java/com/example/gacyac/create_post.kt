package com.example.gacyac

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class CreatePost : AppCompatActivity() {
    private lateinit var saveButton: Button
    private lateinit var postTitleText: EditText
    private lateinit var postBodyText: EditText

    private lateinit var androidID: String
    private lateinit var userID: String

    var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post)

        saveButton = findViewById(R.id.save_post)
        postTitleText = findViewById(R.id.post_title)
        postBodyText = findViewById(R.id.post_text)

        androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)

        //var postCreatorID = findViewById(R.id.postCreatorID)

        database.collection("users").document(androidID).get()
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "data has been retrieved")
                userID = documentReference.get("username").toString()
                Log.d(ContentValues.TAG, "user already exists")

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Could not Log In", e)
            }




        val rootRef = FirebaseFirestore.getInstance()
        val productsRef = rootRef.collection("newererPosts")
        var count = "1"
        productsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let {
                    for (snapshot in it) {
                        count = (Integer.parseInt(count) + 1).toString()
                    }
                }
            } else {
                task.exception?.message?.let {
                    print(it)
                }
            }
        }


        fun saveToDatabase() {
            val postTitle = postTitleText.getText().toString()
            val postBody = postBodyText.getText().toString()
            val timePostCreated = Date(System.currentTimeMillis())


            val newPost = hashMapOf(
                "title" to postTitle,
                "text" to postBody,
                "bonuspoints" to 0,
                "time" to timePostCreated,
                "postID" to count,
                "userID" to userID,
                "AndroidID" to androidID
            )

            database.collection("newererPosts")
                .document(newPost.get("postID").toString()).set(newPost)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }

        saveButton.setOnClickListener {
            saveToDatabase()
            finish()
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, CreatePost::class.java)
        }
    }




}