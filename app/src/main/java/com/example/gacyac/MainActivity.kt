package com.example.gacyac

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.ActivityMainBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    //private lateinit var titleButton: Button
    //private lateinit var editTitle: EditText
    private lateinit var androidID: String
    private lateinit var username: String

    //private val TAG = "testingAuth"


    private lateinit var binding: ActivityMainBinding
    //private lateinit var postItem: Post
    //private lateinit var recyclerView : RecyclerView
    //private lateinit var postAdapter: PostAdapter
    private var database = Firebase.firestore

    private lateinit var listView : RecyclerView
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postRecyclerView.apply{
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = PostAdapter(postList)
        }


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
        //fun changeTitle(titleButton: Button){
        //    val editTextValue: String = titleButton.getText().toString()
        //    editTitle.setText(editTextValue)
        //}


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
        val addButton: Button = findViewById(R.id.btnAddPost)
        addButton.setOnClickListener {
            val intent = CreatePost.newIntent(this)
            startActivity(intent)
        }
        // userDetails.setOnClickListener{
        // val intent =UserProfile.newIntent(this)
        //startActivity(intent)

        //  }

        val bpButton: Button = findViewById(R.id.btnBonusPoints)
        bpButton.setOnClickListener{
            val intent = UserProfile.newIntent(this)
            startActivity(intent)
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

    /*override fun onStart() {
        super.onStart()
    }*/








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