package com.example.petcare.data

data class User(
    var uid: String,
    var name: String? = null,
    var email: String? = null,
    var urlImg: String? = null,
    var createdAt: Long? = 0
)
