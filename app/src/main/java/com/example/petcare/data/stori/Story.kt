package com.example.petcare.data.stori

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Story(
    var postId: String,
    var uid: String? = null,
    var name: String? = null,
    var avatarUrl: String? = null,
    var urlImg: String? = null,
    var description: String? = null,
    var createdAt: Long? = null,
    var comment: Int = 0,
    var share: Int = 0,
    var like: ArrayList<String> = ArrayList<String>()
):Parcelable{
    constructor(): this("", "")
}
