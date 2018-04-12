package com.suyashsrijan.londonmeetup.newcode.app.network.model.bus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BusDepartureModel {

    @SerializedName("stationName")
    @Expose
    var stationName: String? = null

    @SerializedName("lineName")
    @Expose
    var lineName: String? = null

    @SerializedName("destinationName")
    @Expose
    var destinationName: String? = null

    @SerializedName("towards")
    @Expose
    var towards: String? = null

    @SerializedName("expectedArrival")
    @Expose
    var expectedArrival: String? = null

}
