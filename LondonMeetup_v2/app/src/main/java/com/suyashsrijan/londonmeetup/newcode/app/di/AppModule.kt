package com.suyashsrijan.londonmeetup.newcode.app.di

import android.app.Application
import com.suyashsrijan.londonmeetup.newcode.app.di.scopes.PerApplication
import com.suyashsrijan.londonmeetup.newcode.app.repository.preferences.PreferencesRepository
import com.suyashsrijan.londonmeetup.newcode.app.repository.preferences.PreferencesRepositoryImpl
import com.suyashsrijan.londonmeetup.newcode.app.repository.transport.TransportRepository
import com.suyashsrijan.londonmeetup.newcode.app.repository.transport.TransportRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(private val application: Application) {

    @Provides
    @PerApplication
    internal fun provideTransportRepository(repository: TransportRepositoryImpl): TransportRepository {
        return repository
    }

    @Provides
    @PerApplication
    internal fun providePreferencesRepository(repository: PreferencesRepositoryImpl): PreferencesRepository {
        return repository
    }
}