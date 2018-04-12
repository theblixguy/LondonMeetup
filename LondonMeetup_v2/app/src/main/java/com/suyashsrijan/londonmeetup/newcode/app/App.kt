package com.suyashsrijan.londonmeetup.newcode.app

import android.app.Application
import com.suyashsrijan.londonmeetup.newcode.app.di.AppComponent
import com.suyashsrijan.londonmeetup.newcode.app.di.AppModule
import com.suyashsrijan.londonmeetup.newcode.app.di.DaggerAppComponent

class App: Application() {
    companion object {
        private lateinit var app: App

        fun get(): App = app
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        app = this

        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
        appComponent.inject(this)
    }

    fun getAppComponent(): AppComponent? = appComponent
}