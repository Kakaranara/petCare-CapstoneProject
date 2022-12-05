package com.example.petcare.data.remote.retrofit

import com.example.petcare.data.remote.response.PlacesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("petshop;petstore.json")
    fun getListPlaces(
        @Query("autocomplete") autoComplete: Boolean,
        @Query("types") types: String,
        @Query("bbox") bbox: String,
        @Query("access_token") token: String
    ): Call<PlacesResponse>
}