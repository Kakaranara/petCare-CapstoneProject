package com.example.petcare.ui.main.news

import androidx.lifecycle.ViewModel
import com.example.petcare.data.NewsRepository

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
): ViewModel()  {

    fun getNewsResponseLiveData() = repository.getNewsResponseLiveData()
}