package com.example.petcare.data.stori

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    var uid: String? = null,
    var name: String? = null,
    var avatarUrl: String? = null,
    var urlImg: String? = null,
    var description: String? = null,
    var createdAt: Long? = null,
    var like: Int = 0,
    var isLiked: Boolean = false
):Parcelable{

}
