package com.suyashsrijan.londonmeetup.newcode.app.presenter

import android.os.Bundle

interface BasePresenter<View> {
    fun getStateBundle(): Bundle
    fun attachView(view: View)
    fun detachView()
    fun getView(): View?
    fun isViewAttached(): Boolean
    fun onPresenterCreated()
    fun onPresenterDestroy()
}