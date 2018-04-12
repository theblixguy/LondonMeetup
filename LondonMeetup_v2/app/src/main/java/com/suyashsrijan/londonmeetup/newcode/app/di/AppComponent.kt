package com.suyashsrijan.londonmeetup.newcode.app.di

import com.suyashsrijan.londonmeetup.newcode.app.App
import com.suyashsrijan.londonmeetup.newcode.app.di.scopes.PerApplication
import com.suyashsrijan.londonmeetup.newcode.main.di.MainComponent
import com.suyashsrijan.londonmeetup.newcode.main.di.MainModule
import dagger.Component

@PerApplication
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface AppComponent {
    fun inject(application: App)
    fun plus(module: MainModule): MainComponent
}