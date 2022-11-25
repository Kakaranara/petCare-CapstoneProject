package com.example.petcare.data.remote.response

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class News(
    val image: String? = "",
    val source: String? = "",
    val title: String? = "",
    val link: String? = "",
    val category_id: String? = ""
) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
