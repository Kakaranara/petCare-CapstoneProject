package com.example.petcare.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.petcare.data.remote.response.News
import com.example.petcare.data.remote.response.NewsResponse
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class NewsRepository(
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val newsRef: CollectionReference = rootRef.collection("petNews")
){
    fun getNewsResponseLiveData(): LiveData<NewsResponse> {

        val newsResponseLiveData = MutableLiveData<NewsResponse>()

        newsRef.get().addOnCompleteListener { task ->
            val newsResponse = NewsResponse()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    newsResponse.news = result.documents.mapNotNull { snapShot ->
                        snapShot.toObject(News::class.java)
                    }
                }
            } else {
                newsResponse.exception = task.exception
            }
            newsResponseLiveData.value = newsResponse
        }
        return newsResponseLiveData
    }
}