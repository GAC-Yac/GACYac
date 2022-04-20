package com.example.gacyac

import android.content.ContentValues.TAG
import android.media.metrics.Event
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.ActivityMainBinding
import com.google.firebase.firestore.*
import java.sql.Date
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {
    private lateinit var titleButton: Button
    private lateinit var editTitle: EditText
    private lateinit var binding: ActivityMainBinding
    private lateinit var postItem: Post

    private lateinit var recyclerView : RecyclerView
    private lateinit var postAdapter: PostAdapter
    var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postRecyclerView.apply{
            layoutManager = GridLayoutManager(applicationContext, 1)
            adapter = PostAdapter(postList)
        }


        // Displaying Posts
        //fillWithPosts()
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

        EventChangeListener()

    }

    private fun EventChangeListener() {
        database = FirebaseFirestore.getInstance()
        database.collection("newPosts").orderBy("created").
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
