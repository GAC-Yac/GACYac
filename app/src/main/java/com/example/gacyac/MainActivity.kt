package com.example.gacyac

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.ActivityMainBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.sql.Date

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var androidID: String
    private lateinit var username: String
    private lateinit var toolbar : Toolbar

    private lateinit var binding: ActivityMainBinding
    private var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        // randomly creates a username
        fun createRandomUsername(): String{
            val colors = resources.openRawResource(R.raw.colors).bufferedReader().readLines()
            val nouns = resources.openRawResource(R.raw.nouns).bufferedReader().readLines()
            return colors.random().toUpperCase() + " " + nouns.random().toUpperCase()
        }

        //creates a new user and assigns the user a random username
        fun createNewUser(device_id: String): String {
            val username = createRandomUsername()
            val user = hashMapOf(
                "username" to username,
                "bonuspoints" to 0,
                "dateJoined" to Date(System.currentTimeMillis())
            )
            database.collection("users")
                .document(device_id).set(user)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "document added with username $username")
                    val newUserToast = Toast.makeText(this, "Welcome New User!", Toast.LENGTH_SHORT)
                    val newUsernameToast = Toast.makeText(this, "Your random (anonymous) username Is: $username", Toast.LENGTH_LONG)
                    newUserToast.setGravity(Gravity.TOP, 0, 200)
                    newUsernameToast.setGravity(Gravity.TOP, 0, 200)
                    newUserToast.show()
                    newUsernameToast.show()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
            return username
        }

        // should be called on every app start, either logs in user or creates new user
        fun attemptLogin(device_id: String){
            database.collection("users").document(device_id).get()
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "data has been retrieved")
                    if (documentReference!!.get("username") == null){
                        username = createNewUser(device_id)
                        Log.d(ContentValues.TAG, "new user created")
                    }
                    else {
                        username = documentReference.get("username").toString()
                        Log.d(ContentValues.TAG, "user already exists, username is $username")
                        val loginToast = Toast.makeText(this, "Welcome back, $username", Toast.LENGTH_LONG)
                        loginToast.setGravity(Gravity.TOP, 0, 200)
                        loginToast.show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Could not Log In", e)
                }
        }


        // retrieve unique device identifier
        androidID = Secure.getString(getApplicationContext().getContentResolver(),
            Secure.ANDROID_ID)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postRecyclerView.apply{
            layoutManager = GridLayoutManager(applicationContext, 1)
            adapter = PostAdapter(postList)
        }


        // start the log in process with the unique device identifier
        attemptLogin(androidID)


        // button to create a new post
        // Make a post clickable

        val addButton: ImageButton = findViewById(R.id.btnAddPost)
        addButton.setOnClickListener {
            val intent = CreatePost.newIntent(this)
            startActivity(intent)
        }

        val bpButton: ImageButton = findViewById(R.id.btnBonusPoints)
        bpButton.setOnClickListener{
            val intent = UserProfile.newIntent(this)
            startActivity(intent)
        }

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        eventChangeListener()

    }


    private fun eventChangeListener() {
        database = FirebaseFirestore.getInstance()
        database.collection("newerPosts").orderBy("postID").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("ERROR", error.message.toString())
                    return
                }

                for (dc :DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        postList.add(0, dc.document.toObject(Post::class.java))

                    }
                }
            }

        })
    }
}