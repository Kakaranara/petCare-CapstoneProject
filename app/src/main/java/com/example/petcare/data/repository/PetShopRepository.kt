package com.example.petcare.data.repository

import android.util.Log
import com.example.petcare.data.remote.response.PlacesResponse
import com.example.petcare.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.petcare.data.remote.Result

class PetShopRepository(
    private val apiService: ApiService
){

    fun getAllPetShopLocation(box: String, token:String): Flow<Result<PlacesResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getListPlaces("false","poi",box,token)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }
}