package com.suyashsrijan.londonmeetup.newcode.app.network.model.bike

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BikeModel {

    @SerializedName("places")
    @Expose
    val places: List<Place>? = null

}
