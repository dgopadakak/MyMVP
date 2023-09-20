package com.example.mymvp

import io.reactivex.rxjava3.disposables.CompositeDisposable

class Presenter (private val model: Model) {
    private var view: Viewable? = null

    private val disposeBag = CompositeDisposable()

    fun attachView(view: Viewable)
    {
        this.view = view
    }

    fun detachView()
    {
        view = null
    }
}
