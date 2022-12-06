package com.example.petcare.data.remote.response

data class PlacesResponse(
    var message: String? = null,
    var features: ArrayList<Feature>? = null,
    var attribution: String? = null
)

data class Feature (
    var id: String? = null,
    var type: String? = null,
    var properties: Properties? = null,
    var text: String? = null,
    var place_name: String? = null,
    var center: ArrayList<Double>? = null,
)

data class Properties (
    var address: String? = null,
    var category: String? = null
)