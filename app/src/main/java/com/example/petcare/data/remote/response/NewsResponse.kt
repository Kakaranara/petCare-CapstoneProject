package com.example.petcare.data.remote.response

data class NewsResponse(
    var news: List<News>? = null,
    var exception: Exception? = null
)
