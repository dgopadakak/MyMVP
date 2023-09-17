package com.example.mymvp

class Presenter (val model: Model)
{
    private var view: Viewable? = null

    init
    {

    }

    fun attachView(view: Viewable)
    {
        this.view = view
    }

    fun detachView()
    {
        view = null
    }
}