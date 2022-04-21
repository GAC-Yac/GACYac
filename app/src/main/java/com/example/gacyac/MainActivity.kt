package com.example.gacyac

import android.content.ContentValues.TAG
import android.media.metrics.Event
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.ActivityMainBinding
import com.google.firebase.firestore.*
import java.sql.Date
import java.sql.Timestamp


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var titleButton: Button
    private lateinit var editTitle: EditText
    private lateinit var android_id: String
    private lateinit var username: String

    private val TAG = "testingAuth"


    private lateinit var recyclerView : RecyclerView
    private lateinit var postAdapter: PostAdapter
    var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //titleButton = findViewById(R.id.post_title)

        val database = Firebase.firestore

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
                "username" to username
            )
            database.collection("users")
                .document(device_id).set(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "document added with username $username")
                    var newUserToast = Toast.makeText(this, "Welcome New User!", Toast.LENGTH_SHORT)
                    var newUsernameToast = Toast.makeText(this, "Your random (anonymous) username Is: $username", Toast.LENGTH_LONG)
                    newUserToast.setGravity(Gravity.TOP, 0, 200)
                    newUsernameToast.setGravity(Gravity.TOP, 0, 200)
                    newUserToast.show()
                    newUsernameToast.show()
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
                        username = createNewUser(device_id)
                        Log.d(TAG, "new user created")
                    }
                    else {
                        username = documentReference!!.get("username").toString()
                        Log.d(TAG, "user already exists, username is $username")
                        var loginToast = Toast.makeText(this, "Welcome back, $username", Toast.LENGTH_LONG)
                        loginToast.setGravity(Gravity.TOP, 0, 200)
                        loginToast.show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Could not Log In", e)
                }
        }
        //fun changeTitle(titleButton: Button){
        //    val editTextValue: String = titleButton.getText().toString()
        //    editTitle.setText(editTextValue)
        //}


        // retrieve unique device identifier
        android_id = Secure.getString(getApplicationContext().getContentResolver(),
            Secure.ANDROID_ID)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postRecyclerView.apply{
            layoutManager = GridLayoutManager(applicationContext, 1)
            adapter = PostAdapter(postList)
        }


        // start the log in process with the unique device identifier
        attemptLogin(android_id)
        // Displaying Posts
        //fillWithPosts()
        val mainActivity = this


        var addButton: Button = findViewById(R.id.btnAddPost)

        addButton.setOnClickListener {
            val intent = CreatePost.newIntent(this)
            startActivity(intent)
        }

        var bpButton: Button = findViewById(R.id.btnBonusPoints)

        /*bpButton.setOnClickListener{
            postList.clear()
        }*/

        EventChangeListener()

    }

    private fun EventChangeListener() {
        database = FirebaseFirestore.getInstance()
        database.collection("newPosts").orderBy("time").
            addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("ERROR", error.message.toString())
                        return
                    }

                    for (dc :DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            postList.add(dc.document.toObject(Post::class.java))

                        }
                    }
                }

            })
    }

    override fun onStart() {
        super.onStart()
    }








    /*private fun fillWithPosts() {
        val database = Firebase.firestore
        val docRef = database.collection("posts")
        /*
        for(){
            // for each post instance in the database, make a Post object and fill it with the contents from each database post
            val post = Post()
        }*/

        val testPost = Post(
            "Test Post",
            "This is a test post.",
            0,
            "Anonymous",
            Date(System.currentTimeMillis()),
            "0"
        )

        postList.add(testPost)
    }*/

}











































//setContentView(R.layout.activity_main)
//titleButton = findViewById(R.id.post_title)


//fun changeTitle(titleButton: Button){
//    val editTextValue: String = titleButton.getText().toString()
//    editTitle.setText(editTextValue)
//}

//titleButton.setOnClickListener {
//clicking title opens up activity for the post
//}
