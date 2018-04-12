package com.suyashsrijan.londonmeetup.newcode.app.network.model.bus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StopPoint {

    @SerializedName("stopType")
    @Expose
    val stopType: String? = null

    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("commonName")
    @Expose
    val commonName: String? = null

    @SerializedName("indicator")
    @Expose
    val indicator: String? = null

    @SerializedName("lat")
    @Expose
    val lat: Double? = null

    @SerializedName("lon")
    @Expose
    val lon: Double? = null


}
