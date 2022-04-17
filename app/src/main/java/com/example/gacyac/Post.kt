package com.example.gacyac

var postList = mutableListOf<Post>()

class Post(
    var title: String,
    var text: String,
    var bonuspoints: Int,
    var username: String,
    var time: String,
    var id: Int? = postList.size
)