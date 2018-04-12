package com.suyashsrijan.londonmeetup.newcode.app.network.model.tube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.StopPoint

class TubeModel {

    @SerializedName("stopPoints")
    @Expose
    val stopPoints: List<StopPoint>? = null

}
