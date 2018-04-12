package com.suyashsrijan.londonmeetup.newcode.app.network.model.bike

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Place {

    @SerializedName("commonName")
    @Expose
    val commonName: String? = null

    @SerializedName("placeType")
    @Expose
    val placeType: String? = null

    @SerializedName("additionalProperties")
    @Expose
    val additionalProperties: List<AdditionalProperty>? = null

    @SerializedName("lat")
    @Expose
    val lat: Double? = null

    @SerializedName("lon")
    @Expose
    val lon: Double? = null

}
