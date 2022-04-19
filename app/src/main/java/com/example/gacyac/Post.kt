package com.example.gacyac

import com.google.firebase.firestore.FieldValue

var postList = mutableListOf<Post>()

class Post(
    var title: String,
    var text: String,
    var bonuspoints: Int,
    var username: String,
    var time: FieldValue,
    var id: String
)