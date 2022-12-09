package com.example.petcare.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PlacesResponse(
    var message: String? = null,
    var features: List<Feature>? = null,
    var attribution: String? = null
)

@Parcelize
data class Feature (
    var id: String? = null,
    var type: String? = null,
    var properties: Properties? = null,
    var text: String? = null,
    var place_name: String? = null,
    var center: List<Double>? = null,
): Parcelable

@Parcelize
data class ListOfFeature (
    var list: List<Feature>
): Parcelable

@Parcelize
data class Properties (
    var address: String? = null,
    var category: String? = null
): Parcelable