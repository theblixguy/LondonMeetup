package com.suyashsrijan.londonmeetup.newcode.app.network.api

import com.suyashsrijan.londonmeetup.API.tfl.enums.Modes
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bike.BikeModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusDepartureModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeDepartureModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransportForLondonAPI {

    @GET("/Stoppoint")
    fun getNearbyTubes(@Query("app_id") appID: String,
                       @Query("app_key") appKey: String,
                       @Query("stoptypes") stopTypes: Stoptypes,
                       @Query("radius") radius: Int,
                       @Query("useStopPointHierarchy") useStopPointHierarchy: Boolean,
                       @Query("modes") mode: Modes,
                       @Query("categories") categories: String,
                       @Query("returnLines") returnLines: Boolean,
                       @Query("lat") latitude: Double,
                       @Query("lon") longitude: Double): Call<TubeModel>

    @GET("/Stoppoint/{stopId}/Arrival")
    fun getTubeDepartures(@Path("stopId") stopId: String,
                          @Query("app_id") appID: String,
                          @Query("app_key") appKey: String,
                          @Query("radius") radius: Int,
                          @Query("lat") latitude: Double,
                          @Query("lon") longitude: Double): Call<List<TubeDepartureModel>>

    @GET("/Stoppoint")
    fun getNearbyBusStops(@Query("app_id") appID: String,
                          @Query("app_key") appKey: String,
                          @Query("stoptypes") stopTypes: Stoptypes,
                          @Query("radius") radius: Int,
                          @Query("useStopPointHierarchy") useStopPointHierarchy: Boolean,
                          @Query("modes") mode: Modes,
                          @Query("categories") categories: String,
                          @Query("returnLines") returnLines: Boolean,
                          @Query("lat") latitude: Double,
                          @Query("lon") longitude: Double): Call<BusModel>

    @GET("/Stoppoint/{stopId}/Arrival")
    fun getBusDepartures(@Path("stopId") stopId: String,
                         @Query("app_id") appID: String,
                         @Query("app_key") appKey: String,
                         @Query("radius") radius: Int,
                         @Query("lat") latitude: Double,
                         @Query("lon") longitude: Double): Call<List<BusDepartureModel>>

    @GET("/BikePoint")
    fun getNearbyBikes(@Query("app_id") appID: String,
                       @Query("app_key") appKey: String,
                       @Query("radius") radius: Int,
                       @Query("lat") latitude: Double,
                       @Query("lon") longitude: Double): Call<BikeModel>
}