package com.example.mymvp.model

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class Model(private val mqttHelper: MqttHelper)
{
    private val disposeBag = CompositeDisposable()

    fun setLedStatus(status: Boolean): Completable
    {
        return Completable.create { subscriber ->

        }
    }

    fun doLedStatusRequest(): Completable
    {
        return Completable.create { subscriber ->

        }
    }

    fun doTimeRequest(): Completable
    {
        return Completable.create { subscriber ->

        }
    }

    fun ledStatusDataSource(): Observable<Boolean>
    {
        return Observable.create { subscriber ->

        }
    }

    fun timeDataSource(): Observable<String>
    {
        return Observable.create { subscriber ->

        }
    }

    fun connect(): Completable
    {
        return Completable.create { subscriber ->
            disposeBag.add(
                mqttHelper.connect()
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(
                        {
                            subscriber.onComplete()
                        },
                        {
                            subscriber.onError(it)
                        }
                    )
            )
        }
    }

    fun subscribeTopics(): Completable
    {
        return Completable.create { subscriber ->
            disposeBag.add(
                mqttHelper.subscribeTopic()
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(
                        {
                            subscriber.onComplete()
                        },
                        {
                            subscriber.onError(it)
                        }
                    )
            )
        }
    }

    fun kill()
    {
        disposeBag.dispose()
        mqttHelper.disconnect()
    }
}
