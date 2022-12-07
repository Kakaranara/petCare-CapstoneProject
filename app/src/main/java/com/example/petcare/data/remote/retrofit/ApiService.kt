package com.example.petcare.data.remote.retrofit

import com.example.petcare.data.remote.response.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("petshop;petstore.json")
    suspend fun getListPlaces(
        @Query("autocomplete") autoComplete: String,
        @Query("types") types: String,
        @Query("bbox") box: String,
        @Query("access_token") token: String
    ): PlacesResponse

}