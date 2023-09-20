package com.example.mymvp.presenter

import com.example.mymvp.model.Model
import com.example.mymvp.view.Viewable
import io.reactivex.rxjava3.disposables.CompositeDisposable

class Presenter (private val model: Model)
{
    private var view: Viewable? = null
    private val disposeBag = CompositeDisposable()

    fun attachView(view: Viewable)
    {
        this.view = view
        disposeBag.add(
            model.connect()
                .subscribe(
                    {
                        disposeBag.add(
                            model.subscribeTopics()
                                .subscribe(
                                    {
                                        view.changeConnectionStatus(true)
                                    },
                                    {
                                        TODO()
                                    }
                                )
                        )
                    },
                    {
                        TODO()
                    }
                )
        )
    }



    fun detachView()
    {
        view = null
        disposeBag.dispose()
        model.kill()
    }
}
