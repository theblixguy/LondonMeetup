package com.suyashsrijan.londonmeetup.newcode.main.di

import com.suyashsrijan.londonmeetup.newcode.app.di.scopes.PerPresenter
import com.suyashsrijan.londonmeetup.newcode.main.presenter.MainPresenterImpl
import com.suyashsrijan.londonmeetup.newcode.main.view.MainActivity
import dagger.Subcomponent

@PerPresenter
@Subcomponent(modules = [(MainModule::class)])
interface MainComponent {
    fun inject(mainPresenter: MainPresenterImpl)
    fun inject(mainActivity: MainActivity)
}