package com.example.petcare.data.repository

import com.example.petcare.data.remote.Result
import com.example.petcare.data.remote.response.News
import com.example.petcare.data.remote.response.NewsResponse
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class NewsRepository(
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val newsRef: CollectionReference = rootRef.collection("petNews"),
    private val funFactsNewsRef: Query = rootRef.collection("petNews").whereEqualTo("category_id","C0001"),
    private val healthNewsRef: Query = rootRef.collection("petNews").whereEqualTo("category_id","C0002"),
    private val tipsTrickNewsRef: Query = rootRef.collection("petNews").whereEqualTo("category_id","C0003")
){
    suspend fun getAllNewsResponse(): Flow<Result<NewsResponse>> = flow {
        val newsResponse = NewsResponse()
        emit(Result.Loading)
        try{
            newsResponse.news = newsRef.get().await().documents.mapNotNull { snapShot ->
                snapShot.toObject(News::class.java)
            }
            emit(Result.Success(newsResponse))
        }catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getFunFactsNews(): Flow<Result<NewsResponse>> = flow {
        val newsResponse = NewsResponse()
        emit(Result.Loading)
        try{
            newsResponse.news = funFactsNewsRef.get().await().documents.mapNotNull { snapShot ->
                snapShot.toObject(News::class.java)
            }
            emit(Result.Success(newsResponse))
        }catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getHealthNews(): Flow<Result<NewsResponse>> = flow {
        val newsResponse = NewsResponse()
        emit(Result.Loading)
        try{
            newsResponse.news = healthNewsRef.get().await().documents.mapNotNull { snapShot ->
                snapShot.toObject(News::class.java)
            }
            emit(Result.Success(newsResponse))
        }catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getTipsTrickNews(): Flow<Result<NewsResponse>> = flow {
        val newsResponse = NewsResponse()
        emit(Result.Loading)
        try{
            newsResponse.news = tipsTrickNewsRef.get().await().documents.mapNotNull { snapShot ->
                snapShot.toObject(News::class.java)
            }
            emit(Result.Success(newsResponse))
        }catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getSearchNews(name : String): Flow<Result<NewsResponse>> = flow {
//        val searchRef: Query = rootRef.collection("petNews").whereGreaterThanOrEqualTo("title", name)
//            .whereLessThanOrEqualTo("title", name + "\uf8ff")
        val newsResponse = NewsResponse()
        emit(Result.Loading)
        try{
            var allNews = newsRef.get().await().documents.mapNotNull { snapShot ->
                snapShot.toObject(News::class.java)
            }
            allNews = allNews.filter { news ->
                news.title!!.contains(name,true)
            }
            newsResponse.news = allNews
            emit(Result.Success(newsResponse))
        }catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }
}