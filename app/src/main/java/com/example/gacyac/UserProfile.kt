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
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import java.sql.Date
import java.text.SimpleDateFormat
import android.os.Parcelable

class UserProfile : AppCompatActivity() {
    private var database = Firebase.firestore
    private lateinit var username: TextView
    private lateinit var dateJoin: TextView
    private lateinit var androidID: String
    private lateinit var UserToolbar: Toolbar
    private lateinit var bonusPoints: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)

        UserToolbar = findViewById(R.id.user_toolbar)
        setSupportActionBar(UserToolbar)

        androidID = Secure.getString(getApplicationContext().getContentResolver(),
            Secure.ANDROID_ID)
        eventChangeListener(androidID)

        val backButton: ImageButton = findViewById(R.id.back_button_user)
        backButton.setOnClickListener{
            finish()
        }

    }


    private fun eventChangeListener(device_id: String) {
        database = FirebaseFirestore.getInstance()
        // var currentUser = database.collection("users").get().toString()
        //var dateJoined = database.collection("users").get().toString()
        username = findViewById(R.id.username)
        dateJoin = findViewById(R.id.dateJoinTextView)
        bonusPoints = findViewById(R.id.bonusPoints)
        // username.setHint(currentUser)
        database.collection("users").document(device_id).get()
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "data has been retrieved")
                val usernameChange = documentReference.get("username").toString()
                Log.d(ContentValues.TAG, "user already exists, username is $username")
                val timestamp= documentReference.get("dateJoined") as com.google.firebase.Timestamp
                val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                val sdf = SimpleDateFormat("MM/dd/yyyy")
                val netDate = Date(milliseconds)
                val date = sdf.format(netDate).toString()
                val format = "Date Joined: $date"
                val bp = documentReference.get("bonuspoints").toString()
                Log.d("TAG170", date)
                dateJoin.setText(format)
                username.setHint(usernameChange)
                bonusPoints.setText("Bonus Points: $bp")

            }

            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Could not Log In", e)
            }
    }





    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, UserProfile::class.java)
        }
    }
}