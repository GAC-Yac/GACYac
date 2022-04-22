package com.example.gacyac

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.provider.Settings.Secure
import java.sql.Date

class UserProfile : AppCompatActivity() {
    private var database = Firebase.firestore
    private lateinit var username: TextView
    private lateinit var dateJoin: TextView
    private lateinit var androidID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)
        androidID = Secure.getString(getApplicationContext().getContentResolver(),
            Secure.ANDROID_ID)
        eventChangeListener(androidID)

    }


    private fun eventChangeListener(device_id: String) {
        database = FirebaseFirestore.getInstance()
        // var currentUser = database.collection("users").get().toString()
        //var dateJoined = database.collection("users").get().toString()
        username = findViewById(R.id.username)
        dateJoin = findViewById(R.id.dateJoinTextView)
        // username.setHint(currentUser)
        database.collection("users").document(device_id).get()
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "data has been retrieved")
                val usernameChange = documentReference.get("username").toString()
                Log.d(ContentValues.TAG, "user already exists, username is $username")
                username.setHint(usernameChange)

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Could not Log In", e)
            }


        // TODO: Luke, get this working with the user profiles. If needed, reference eventChangeListener in MainActivity.kt

        //currentUser.toObject(User::class.java)
    }





    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, UserProfile::class.java)
        }
    }
}
