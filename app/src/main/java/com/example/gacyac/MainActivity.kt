package com.example.gacyac

import android.annotation.SuppressLint
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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gacyac.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import layout.ButtonHighlighterOnTouchListener
import org.w3c.dom.Text
import layout.ButtonHighlighterOnTouchListener2
import java.sql.Date
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity()  {
    private lateinit var androidID: String
    private lateinit var username: TextView
    private lateinit var username1: String
    private lateinit var binding: ActivityMainBinding
    private lateinit var dateJoin: TextView
    private lateinit var topUsers: TextView
    private lateinit var bonusPoints: TextView

    private lateinit var currentPostCount: TextView
    private var database = Firebase.firestore


    private var toolbar : Toolbar? = null
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
                "dateJoined" to Date(System.currentTimeMillis()),
                "AndroidIdentifier" to androidID
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
                        username1 = createNewUser(device_id)
                        Log.d(ContentValues.TAG, "new user created")
                    } else {
                        username1 = documentReference.get("username").toString()
                        val loginToast =
                            Toast.makeText(this, "Welcome back, $username1", Toast.LENGTH_LONG)
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
        val addButton: ImageButton = findViewById(R.id.btnAddPost)
        addButton.setOnClickListener {
            val intent = CreatePost.newIntent(this)
            startActivity(intent)
        }

        val profile_layout = findViewById<View>(R.id.profile_id) as View
        val leaderboard_layout = findViewById<View>(R.id.leaderboard_id) as View

        val bpButton: ImageButton = findViewById(R.id.btnBonusPoints)
        bpButton.setOnClickListener{
            val animationIn = com.google.android.material.R.anim.abc_slide_in_top
            val animationOut = com.google.android.material.R.anim.abc_slide_out_top
            if (profile_layout.isVisible){
                profile_layout.startAnimation(AnimationUtils.loadAnimation(this, animationOut))
                profile_layout.visibility = View.INVISIBLE
            }
            else{
                if (leaderboard_layout.isVisible){
                    val animationOut = com.google.android.material.R.anim.abc_slide_out_top
                    leaderboard_layout.startAnimation(AnimationUtils.loadAnimation(this, animationOut))
                    leaderboard_layout.visibility = View.INVISIBLE
                }
                profile_layout.startAnimation(AnimationUtils.loadAnimation(this, animationIn))
                profile_layout.visibility = View.VISIBLE

            }
        }

        val leaderboardButton: ImageButton = findViewById(R.id.btnLeaderboard)
        leaderboardButton.setOnClickListener{
            val animationIn = com.google.android.material.R.anim.abc_slide_in_top
            val animationOut = com.google.android.material.R.anim.abc_slide_out_top
            if (leaderboard_layout.isVisible){
                leaderboard_layout.startAnimation(AnimationUtils.loadAnimation(this, animationOut))
                leaderboard_layout.visibility = View.INVISIBLE
            }
            else{
                if (profile_layout.isVisible){
                    val animationOut = com.google.android.material.R.anim.abc_slide_out_top
                    profile_layout.startAnimation(AnimationUtils.loadAnimation(this, animationOut))
                    profile_layout.visibility = View.INVISIBLE
                }
                leaderboard_layout.startAnimation(AnimationUtils.loadAnimation(this, animationIn))
                leaderboard_layout.visibility = View.VISIBLE

            }
        }


        val leaderboardRemovalButton: Button = findViewById(R.id.leaderboardRemoveBtn)
        leaderboardRemovalButton.setOnTouchListener(ButtonHighlighterOnTouchListener(leaderboardRemovalButton))
        leaderboardRemovalButton.setOnClickListener{
            val leaderboard_layout = findViewById<View>(R.id.leaderboard_id) as View
            if (leaderboard_layout.isVisible){
                val animationOut = com.google.android.material.R.anim.abc_slide_out_top
                leaderboard_layout.startAnimation(AnimationUtils.loadAnimation(this, animationOut))
                leaderboard_layout.visibility = View.INVISIBLE
            }
        }

        val profileRemovalButton: Button = findViewById(R.id.profileRmvBtn)
        profileRemovalButton.setOnTouchListener(ButtonHighlighterOnTouchListener(profileRemovalButton))
        profileRemovalButton.setOnClickListener{
            val profile_layout = findViewById<View>(R.id.profile_id) as View
            if (profile_layout.isVisible){
                val animationOut = com.google.android.material.R.anim.abc_slide_out_top
                profile_layout.startAnimation(AnimationUtils.loadAnimation(this, animationOut))
                profile_layout.visibility = View.INVISIBLE
            }
        }

        eventChangeListener(androidID)
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

    private fun getProfileInformation(device_id: String){
        database = FirebaseFirestore.getInstance()
        username = findViewById(R.id.username)
        dateJoin = findViewById(R.id.dateJoinTextView)
        bonusPoints = findViewById(R.id.bonusPoints)
        // username.setHint(currentUser)
        database.collection("users").document(device_id).get()
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "data has been retrieved")
                val usernameChange = documentReference.get("username").toString()
                Log.d(ContentValues.TAG, "user already exists, username is $username")
                val timestamp = documentReference.get("dateJoined") as com.google.firebase.Timestamp
                val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                val sdf = SimpleDateFormat("MM/dd/yyyy")
                val netDate = Date(milliseconds)
                val date = sdf.format(netDate).toString()
                val format = "Date Joined: $date"
                val bp = documentReference.get("bonuspoints").toString()
                Log.d("TAG170", date)
                dateJoin.setText(format)
                username.setText(usernameChange)
                bonusPoints.setText("Bonus Points: $bp")

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Could not Log In", e)
            }
    }


    private fun eventChangeListener(device_id: String) {
        database = FirebaseFirestore.getInstance()
        database.collection("newererPosts").orderBy("postID").
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
        getProfileInformation(device_id)
        //getLeaderboardInformation()
        getNavBarInfo()
    }

    private fun getLeaderboardInformation() {
        database = FirebaseFirestore.getInstance()

        database.collection("users")
            .orderBy("bonuspoints", Query.Direction.DESCENDING).limit(5)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("LUKEleaderboard2", "LUKEleaderboard2")
                Log.d("documents: ", "${documents.documents.toString()}")
                topUsers = findViewById(R.id.topUsers)
                topUsers.setText("" +
                        "1. ${documents.documents.get(0).get("username")}\n" +
                        "--->(Bonus Points: ${documents.documents.get(0).get("bonuspoints")})\n" +
                        "2. ${documents.documents.get(1).get("username")}\n" +
                        "--->(Bonus Points: ${documents.documents.get(1).get("bonuspoints")})\n" +
                        "3. ${documents.documents.get(2).get("username")}\n" +
                        "--->(Bonus Points: ${documents.documents.get(2).get("bonuspoints")})\n" +
                        "4. ${documents.documents.get(3).get("username")}\n" +
                        "--->(Bonus Points: ${documents.documents.get(3).get("bonuspoints")})\n" +
                        "5. ${documents.documents.get(4).get("username")}\n" +
                        "--->(Bonus Points: ${documents.documents.get(4).get("bonuspoints")})")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                Log.i("LUKEleaderboardDEBUG",exception.toString())
            }
    }

    private fun getNavBarInfo() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.navigation_header_username)
        database.collection("users").document(androidID).get()
            .addOnSuccessListener { documentReference ->
                val grabUserName = documentReference.get("username").toString()
                navUsername.text = grabUserName
            }

        database.collection("newererPosts").orderBy("postID", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener { documents ->
            currentPostCount = headerView.findViewById(R.id.navigation_header_current_post_count)
            currentPostCount.setText("Current Post Count: ${documents.documents.get(0).get("postID")}")
            //${documents.documents.get(0).get("postID")}
        }

        val lastRefreshed : TextView = headerView.findViewById(R.id.navigation_header_last_refreshed)
        val getCurrentTime = Date(System.currentTimeMillis())

        lastRefreshed.setText("Last Refreshed: $getCurrentTime")

    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    // helper function for sidebar
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
        }
    }*/





}