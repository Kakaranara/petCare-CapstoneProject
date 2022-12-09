package com.example.petcare.ui.main.other.petshop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.remote.Result
import com.example.petcare.data.remote.response.PlacesResponse
import com.example.petcare.data.repository.PetShopRepository
import kotlinx.coroutines.launch

class PetShopViewModel(
    private val repository: PetShopRepository,
): ViewModel() {

    private val _listPetShop = MutableLiveData<Result<PlacesResponse>>()
    val listPetShop: LiveData<Result<PlacesResponse>> = _listPetShop

    fun getPetShopList(lat: Double, lon: Double, token: String){
        val rectangle = 500
        val degreeDeltaLat = 0.001 * (rectangle / 2.0) * kotlin.math.cos(lon)
        val degreeDeltaLon = 0.001 * (rectangle / 2.0) * kotlin.math.cos(lat)
        var minLat = lat - degreeDeltaLat
        var maxLat = lat + degreeDeltaLat
        var minLon = lon - degreeDeltaLon
        var maxLon = lon + degreeDeltaLon
        if(minLat > maxLat){
            val minNow = minLat
            minLat = maxLat
            maxLat = minNow
        }
        if(minLon > maxLon){
            val minNow = minLon
            minLon = maxLon
            maxLon = minNow
        }
        val boxFinal = "$minLon,$minLat,$maxLon,$maxLat"

        viewModelScope.launch {
            repository.getAllPetShopLocation(boxFinal, token).collect{
                _listPetShop.value = it
            }
        }
    }
}