package com.example.gacyac

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)
    }
    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, UserProfile::class.java)
        }
    }
}
