package com.example.petcare.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.petcare.data.remote.response.News
import com.example.petcare.data.remote.response.NewsResponse
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NewsRepository(
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val newsRef: CollectionReference = rootRef.collection("petNews"),
    private val funFactsNewsRef: Query = rootRef.collection("petNews").whereEqualTo("category_id","C0001"),
    private val healthNewsRef: Query = rootRef.collection("petNews").whereEqualTo("category_id","C0002"),
    private val tipsTrickNewsRef: Query = rootRef.collection("petNews").whereEqualTo("category_id","C0003")
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

    fun getFunFactsNewsLiveData(): LiveData<NewsResponse> {

        val funFactsNewsResponseLiveData = MutableLiveData<NewsResponse>()

        funFactsNewsRef.get().addOnCompleteListener { task ->
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
            funFactsNewsResponseLiveData.value = newsResponse
        }
        return funFactsNewsResponseLiveData
    }

    fun getHealthNewsLiveData(): LiveData<NewsResponse> {

        val healthNewsResponseLiveData = MutableLiveData<NewsResponse>()

        healthNewsRef.get().addOnCompleteListener { task ->
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
            healthNewsResponseLiveData.value = newsResponse
        }
        return healthNewsResponseLiveData
    }

    fun getTipsTrickNewsLiveData(): LiveData<NewsResponse> {

        val tipsTrickNewsResponseLiveData = MutableLiveData<NewsResponse>()

        tipsTrickNewsRef.get().addOnCompleteListener { task ->
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
            tipsTrickNewsResponseLiveData.value = newsResponse
        }
        return tipsTrickNewsResponseLiveData
    }
}