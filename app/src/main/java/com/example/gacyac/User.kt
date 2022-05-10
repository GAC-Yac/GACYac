package com.example.gacyac

import java.util.*

// initialzes user attributes in database to be called on profile create
class User (
    var username: String ?= null,
    var bonuspoints: Int ?= null,
    var dateJoined: Date ?= null
)