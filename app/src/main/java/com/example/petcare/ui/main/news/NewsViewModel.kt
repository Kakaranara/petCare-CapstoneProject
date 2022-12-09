package com.example.petcare.ui.main.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.remote.Result
import com.example.petcare.data.remote.response.NewsResponse
import com.example.petcare.data.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository(),
): ViewModel()  {

    private val _listNews = MutableLiveData<Result<NewsResponse>>()
    val listNews: LiveData<Result<NewsResponse>> = _listNews

    private val _searchListNews = MutableLiveData<Result<NewsResponse>>()
    val searchListNews: LiveData<Result<NewsResponse>> = _searchListNews

    fun getNewsHandler(index: Int){
        viewModelScope.launch {
            when(index){
                1 -> repository.getAllNewsResponse().collect{
                    _listNews.value = it
                }
                2 -> repository.getHealthNews().collect{
                    _listNews.value = it
                }
                3 -> repository.getFunFactsNews().collect{
                    _listNews.value = it
                }
                4 -> repository.getTipsTrickNews().collect{
                    _listNews.value = it
                }
            }
        }
    }

    fun getSearchNewsLiveData(name: String) {
        viewModelScope.launch {
            repository.getSearchNews(name).collect(){
                _searchListNews.value = it
            }
        }
    }

    /*
        * Commented for possibility using it in the future, do not erase it just yet.
        * */
//    fun getNewsResponseLiveData() {
//        viewModelScope.launch {
//            repository.getAllNewsResponse().collect(){
//                _listNews.value = it
//            }
//        }
//
//    }
//
//    fun getFunNewsResponseLiveData() = runBlocking {
//        repository.getFunFactsNews().asLiveData()
//    }
//
//    fun getHealthNewsResponseLiveData() = runBlocking {
//        repository.getHealthNews().asLiveData()
//    }
//
//    fun getTipsTrickResponseLiveData() = runBlocking {
//        repository.getTipsTrickNews().asLiveData()
//    }

}