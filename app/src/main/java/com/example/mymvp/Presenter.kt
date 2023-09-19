package com.example.mymvp

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class Presenter (private val model: Model)
{
    private var view: Viewable? = null

    private val disposeBag = CompositeDisposable()

    init
    {
        disposeBag.add(
            model.mqttHelper.connect()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.i("IWTSI", "Connected")
                        disposeBag.add(
                            model.mqttHelper.subscribeTopic()
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        Log.i("IWTSI", "Subscribed")
                                        disposeBag.add(
                                            model.mqttHelper.receiveMessages()
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(
                                                    {
                                                        Log.i("IWTSI", "Message: $it")
                                                    },
                                                    {
                                                        Log.e("IWTSI", "Msg err: $it")
                                                    }
                                                )
                                        )
                                    },
                                    {
                                        Log.e("IWTSI", "Not subscribed: $it")
                                    }
                                )
                        )
                    },
                    {
                        Log.e("IWTSI", "Not connected: $it")
                    }
                )
        )
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