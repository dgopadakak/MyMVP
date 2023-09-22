package com.example.mymvp.model

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class Model(private val mqttHelper: MqttHelper)
{
    companion object
    {
        private const val TO_LED_TOPIC = "to/led"
        private const val TO_TIME_TOPIC = "to/time"
    }
    private val disposeBag = CompositeDisposable()

    fun setLedStatus(status: Boolean): Completable
    {
        return Completable.create { subscriber ->
            disposeBag.add(
                mqttHelper.publishMessages(TO_LED_TOPIC, "set $status")
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

    fun doLedStatusRequest(): Completable
    {
        return Completable.create { subscriber ->
            disposeBag.add(
                mqttHelper.publishMessages(TO_LED_TOPIC, "request")
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

    fun doTimeRequest(): Completable
    {
        return Completable.create { subscriber ->
            disposeBag.add(
                mqttHelper.publishMessages(TO_TIME_TOPIC, "request")
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

    fun dataSource(): Observable<Pair<String, String>>
    {
        return Observable.create { subscriber ->
            disposeBag.add(
                mqttHelper.receiveMessages()
                    .subscribe(
                        {
                            subscriber.onNext(it)
                        },
                        {
                            subscriber.onError(it)
                        }
                    )
            )
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
        disposeBag.add(mqttHelper.disconnect().subscribe())
        disposeBag.dispose()
    }
}
