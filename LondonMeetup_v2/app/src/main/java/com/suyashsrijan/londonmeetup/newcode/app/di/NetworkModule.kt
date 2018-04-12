package com.suyashsrijan.londonmeetup.newcode.app.di

import com.google.gson.GsonBuilder
import com.suyashsrijan.londonmeetup.newcode.app.di.scopes.PerApplication
import com.suyashsrijan.londonmeetup.newcode.app.network.api.TransportForLondonAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    @PerApplication
    fun provideTransportForLondonAPI(): TransportForLondonAPI {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .baseUrl("https://api.tfl.gov.uk")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TransportForLondonAPI::class.java)
    }

}