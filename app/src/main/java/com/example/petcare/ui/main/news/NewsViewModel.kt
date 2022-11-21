package com.example.petcare.ui.main.news

import androidx.lifecycle.ViewModel
import com.example.petcare.data.repository.NewsRepository

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
): ViewModel()  {

    fun getNewsResponseLiveData() = repository.getNewsResponseLiveData()

    fun getFunNewsResponseLiveData() = repository.getFunFactsNewsLiveData()

    fun getHealthNewsResponseLiveData() = repository.getHealthNewsLiveData()

    fun getTipsTrickResponseLiveData() = repository.getTipsTrickNewsLiveData()

}