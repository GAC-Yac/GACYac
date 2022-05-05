package com.example.gacyac

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gacyac.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Date

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity()  {
    private lateinit var androidID: String
    private lateinit var username: String
    private lateinit var binding: ActivityMainBinding
    private var database = Firebase.firestore

    private  var toolbar : Toolbar? = null
    private var mDrawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)






        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       toolbar = findViewById<Toolbar>(R.id.homeToolbar)
        setSupportActionBar(toolbar)

       initNavigationDrawer()
        // randomly creates a username
        fun createRandomUsername(): String {
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
                    val newUsernameToast = Toast.makeText(
                        this,
                        "Your random (anonymous) username Is: $username",
                        Toast.LENGTH_LONG
                    )
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
        fun attemptLogin(device_id: String) {
            database.collection("users").document(device_id).get()
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "data has been retrieved")
                    if (documentReference!!.get("username") == null) {
                        username = createNewUser(device_id)
                        Log.d(ContentValues.TAG, "new user created")
                    } else {
                        username = documentReference.get("username").toString()
                        Log.d(ContentValues.TAG, "user already exists, username is $username")
                        val loginToast =
                            Toast.makeText(this, "Welcome back, $username", Toast.LENGTH_LONG)
                        loginToast.setGravity(Gravity.TOP, 0, 200)
                        loginToast.show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Could not Log In", e)
                }
        }


        // retrieve unique device identifier
        androidID = Secure.getString(
            getApplicationContext().getContentResolver(),
            Secure.ANDROID_ID
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postRecyclerView.apply {
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
        bpButton.setOnClickListener {
            val intent = UserProfile.newIntent(this)
            startActivity(intent)
        }

        val leaderboardButton: ImageButton = findViewById(R.id.btnLeaderboard)
        leaderboardButton.setOnClickListener{
            val leaderboard_layout = findViewById<View>(R.id.leaderboard_id) as View
            leaderboard_layout.visibility = View.VISIBLE
        }


        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.navigation_header_username)
        database.collection("users").document(androidID).get()
            .addOnSuccessListener { documentReference ->
                val grabUserName = documentReference.get("username").toString()
                navUsername.text = grabUserName
            }

        eventChangeListener()
    }


    private fun initNavigationDrawer() {

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            true
        }
        val header = navigationView.getHeaderView(0)
        mDrawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
        }

        mDrawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

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
    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }





}