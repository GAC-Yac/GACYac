package com.example.gacyac

var commentList = mutableListOf<Comment>()

class Comment(
    var text: String ?= null,
    var userID: String ?= null
)