package com.example.gacyac

import com.google.firebase.firestore.FieldValue
import java.sql.Date
import java.sql.Timestamp

var postList = mutableListOf<Post>()

class Post(
    var title: String,
    var text: String,
    var bonuspoints: Int,
    var username: String,
    var time: Date,
    var id: String
)