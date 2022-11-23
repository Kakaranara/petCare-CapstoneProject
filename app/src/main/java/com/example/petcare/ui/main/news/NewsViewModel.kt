package com.example.petcare.ui.main.news

import androidx.lifecycle.*
import com.example.petcare.data.repository.NewsRepository
import kotlinx.coroutines.runBlocking

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
): ViewModel()  {

    /*
        * Commented for possibility using it in the future, do not erase it just yet.
        * */
//    private val _listNews = MutableLiveData<Result<NewsResponse>>()
//    val listNews: LiveData<Result<NewsResponse>> = _listNews
//
//    fun getNewsHandler(index: Int){
//        viewModelScope.launch {
//            when(index){
//                1 -> repository.getAllNewsResponse().collect(){
//                    _listNews.value = it
//                }
//                2 -> repository.getHealthNews().collect(){
//                    _listNews.value = it
//                }
//                3 -> repository.getFunFactsNews().collect(){
//                    _listNews.value = it
//                }
//                4 -> repository.getTipsTrickNews().collect(){
//                    _listNews.value = it
//                }
//            }
//        }
//    }

    fun getNewsResponseLiveData() = runBlocking {
        repository.getAllNewsResponse().asLiveData()

    }

    fun getFunNewsResponseLiveData() = runBlocking {
        repository.getFunFactsNews().asLiveData()
    }

    fun getHealthNewsResponseLiveData() = runBlocking {
        repository.getHealthNews().asLiveData()
    }

    fun getTipsTrickResponseLiveData() = runBlocking {
        repository.getTipsTrickNews().asLiveData()
    }

}