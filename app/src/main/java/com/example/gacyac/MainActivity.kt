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
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gacyac.databinding.ActivityMainBinding
import com.example.gacyac.databinding.PostItemBinding

class MainActivity : AppCompatActivity() {
    private lateinit var titleButton: Button
    private lateinit var editTitle: EditText
    private lateinit var binding: ActivityMainBinding
    private lateinit var postItem: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postRecyclerView.apply{
            layoutManager = GridLayoutManager(applicationContext, 1)
            adapter = PostAdapter(postList)
        }

        //setContentView(R.layout.activity_main)
        //titleButton = findViewById(R.id.post_title)

        val database = Firebase.firestore
        val user = hashMapOf(
            "first" to "Big",
            "last" to "Z",
            "pulls" to true
        )

        database.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        //fun changeTitle(titleButton: Button){
        //    val editTextValue: String = titleButton.getText().toString()
        //    editTitle.setText(editTextValue)
        //}

        //titleButton.setOnClickListener {
        //clicking title opens up activity for the post
        //}


        // Displaying Posts
        fillWithPosts()
        val mainActivity = this

        // Button Functionality
        var addButton: Button = findViewById(R.id.btnAddPost)

        addButton.setOnClickListener {
            val intent = CreatePost.newIntent(this)
            startActivity(intent)
        }

        var bpButton: Button = findViewById(R.id.btnBonusPoints)

        /*bpButton.setOnClickListener{
            postList.clear()
        }*/

    }

    private fun fillWithPosts() {
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
            "Today",
            "0"
        )

        postList.add(testPost)
    }

}
