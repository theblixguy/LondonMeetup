package com.suyashsrijan.londonmeetup.newcode.app.network.model.bus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BusModel {

    @SerializedName("stopPoints")
    @Expose
    val stopPoints: List<StopPoint>? = null

}
