package com.example.gacyac

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserProfile : AppCompatActivity() {
    private var database = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)


    }

    private fun eventChangeListener() {
        database = FirebaseFirestore.getInstance()
        var currentUser = database.collection("users").get()

        // TODO: Luke, get this working with the user profiles. If needed, reference eventChangeListener in MainActivity.kt 
        //currentUser.toObject(User::class.java)
    }




    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, UserProfile::class.java)
        }
    }
}
