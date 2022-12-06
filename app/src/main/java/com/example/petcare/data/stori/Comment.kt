package com.example.petcare.data.stori

data class Comment(
    var idComment: String,
    var idPost: String? = null,
    var uid: String? = null,
    var avatarUrl: String? = null,
    var name: String? = null,
    var comment: String? = null,
    var createdAt: String? = null
){
    constructor(): this("", "")
}
