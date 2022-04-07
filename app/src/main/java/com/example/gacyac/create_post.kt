package com.example.gacyac

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class CreatePost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post)
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, CreatePost::class.java)
        }
    }




}