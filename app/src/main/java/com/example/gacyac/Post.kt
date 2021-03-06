package com.example.gacyac

import java.util.Date


var postList = mutableListOf<Post>()

// database criteria being stored for each individual post in database
class Post(
    var title: String ?= null,
    var text: String ?= null,
    var bonuspoints: Int ?= null,
    var username: String ?= null,
    var time: Date ?= null,
    var userID: String ?= null,
    var postID: String ?= null,
    var androidID: String ?= null
)