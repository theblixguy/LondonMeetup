package com.suyashsrijan.londonmeetup.newcode.app.presenter

import android.os.Bundle
import android.support.annotation.CallSuper

abstract class BasePresenterImpl<View> : BasePresenter<View> {

    private var viewInstance: View? = null
    private var stateBundle: Bundle? = null

    protected fun injectDependencies() {
        onInjectDependencies()
    }

    protected open fun onInjectDependencies() {}
    protected open fun onViewAttached(view: View) {}

    override fun getStateBundle(): Bundle {
        stateBundle?.let {
            return it
        }

        stateBundle = Bundle()
        return stateBundle!!
    }

    override fun attachView(view: View) {
        this.viewInstance = view;
    }

    override fun detachView() {
        viewInstance = null
    }

    override fun getView(): View? {
        return viewInstance
    }

    override fun isViewAttached(): Boolean {
        return viewInstance != null
    }

    override fun onPresenterCreated() {

    }

    @CallSuper
    override fun onPresenterDestroy() {
        stateBundle?.let {
            if (!it.isEmpty) {
                it.clear()
            }
        }
    }
}