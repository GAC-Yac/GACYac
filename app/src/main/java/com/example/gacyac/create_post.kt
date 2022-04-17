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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CreatePost : AppCompatActivity() {
    private lateinit var saveButton: Button
    private lateinit var postTitleText: EditText
    private lateinit var postBodyText: EditText
    private lateinit var postDateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post)

        saveButton = findViewById(R.id.save_post)
        postTitleText = findViewById(R.id.post_title)
        postBodyText = findViewById(R.id.post_text)
        postDateButton = findViewById(R.id.post_date)
        val database = Firebase.firestore



        fun saveToDatabase() {
            var postTitle = postTitleText.getText().toString()
            var postBody = postBodyText.getText().toString()



            val newPost = hashMapOf(
                "title" to postTitle,
                "body" to postBody
            )

            val toastText = Toast.makeText(this, "Sending", Toast.LENGTH_LONG)
            toastText.show()

            database.collection("posts")
                .add(newPost)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }

            val newToast = Toast.makeText(this, "DONE", Toast.LENGTH_LONG)
            newToast.show()
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