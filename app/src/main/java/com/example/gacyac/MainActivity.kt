package com.example.gacyac

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.Intent
import android.provider.Settings.Secure;
import android.util.LogPrinter
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity() {
    private lateinit var titleButton: Button
    private lateinit var editTitle: EditText
    private lateinit var android_id: String
    private lateinit var username: String

    private val TAG = "testingAuth"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //titleButton = findViewById(R.id.post_title)

        val database = Firebase.firestore

        // randomly creates a username
        fun createRandomUsername(): String{
            return "HERE's AN EXAMPLE RANDOM USERNAME"
        }

        //creates a new user and assigns the user a random username
        fun createNewUser(device_id: String): String {
            val username = createRandomUsername()
            val user = hashMapOf(
                "username" to username
            )
            database.collection("users")
                .document(device_id).set(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "document added with username $username")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            return username;
        }

        // should be called on every app start, either logs in user or creates new user
        fun attemptLogin(device_id: String){
            database.collection("users").document(device_id).get()
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "data has been retrieved")
                    if (documentReference!!.get("username") == null){
                        createNewUser(device_id)
                        Log.d(TAG, "new user created")
                    }
                    else {
                        Log.d(TAG, "user already exists, username is ${
                            documentReference!!.get("username")
                        }")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Could not Log In", e)
                }
        }


        // retrieve unique device identifier
        android_id = Secure.getString(getApplicationContext().getContentResolver(),
            Secure.ANDROID_ID)

        // start the log in process with the unique device identifier
        attemptLogin(android_id)


        var addButton: Button = findViewById(R.id.btnAddPost)

        addButton.setOnClickListener {
            val intent = CreatePost.newIntent(this)
            startActivity(intent)
        }
    }

}
