package com.example.gacyac

import com.google.firebase.firestore.FieldValue
import java.sql.Date
import java.sql.Timestamp

var postList = mutableListOf<Post>()

class Post(
    var title: String ?= null,
    var text: String ?= null,
    var bonuspoints: Int ?= null,
    var username: String ?= null,
    var time: Date ?= null
    //var id: String ?= null
)