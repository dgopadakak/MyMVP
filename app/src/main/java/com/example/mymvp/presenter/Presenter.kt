package com.example.mymvp.presenter

import com.example.mymvp.R
import com.example.mymvp.model.Model
import com.example.mymvp.view.Viewable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class Presenter (private val model: Model)
{
//    companion object
//    {
//        private const val FROM_LED_TOPIC = "from/led"
//        private const val FROM_TIME_TOPIC = "from/time"
//    }
    private var view: Viewable? = null
    private val disposeBag = CompositeDisposable()

    private var attemptsToConnect = 0
    private var attemptsToSubscribe = 0
    private var attemptsToPublish = 0
    private var timeReceived = false
    private var ledStatusReceived = false
    private var ledStatusWaiting = false
    private var ledStatus = false

    fun attachView(view: Viewable)
    {
        this.view = view

        view.changeConnectionStatus(1)  // 0 - ошибка, 1 - подключение, 2 - подключено
        startConnection()
    }

    fun ledChipClicked()
    {
        view!!.changeChipEnabled(false)
        ledStatusWaiting = true
        disposeBag.add(
            model.setLedStatus(!ledStatus)
                .subscribe(
                    {
                        view!!.makeToast(R.string.publish_done.toString())
                    },
                    {
                        view!!.makeToast(R.string.publish_error.toString())
                        view!!.changeChipEnabled(true)
                        ledStatusWaiting = false
                    }
                )
        )
    }

    fun reconnectButtonClicked()
    {
        TODO()
    }

    private fun startConnection()
    {
        view!!.changeConnectionStatus(1)
        disposeBag.add(
            model.connect()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        attemptsToConnect = 0
                        startSubscription()
                    },
                    {
                        if (attemptsToConnect <= 3)
                        {
                            attemptsToConnect++
                            startConnection()
                        }
                        else
                        {
                            showError()
                        }
                    }
                )
        )
    }

    private fun startSubscription()
    {
        disposeBag.add(
            model.subscribeTopics()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        attemptsToSubscribe = 0
                        model.startListening()
                        createSubscribers()
                        doRequests()
                    },
                    {
                        if (attemptsToSubscribe <= 3)
                        {
                            attemptsToSubscribe++
                            startSubscription()
                        }
                        else
                        {
                            showError()
                        }
                    }
                )
        )
    }

    private fun doRequests()
    {
        disposeBag.add(
            model.doTimeRequest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        attemptsToPublish = 0
                    },
                    {
                        if (attemptsToPublish <= 3)
                        {
                            doRequests()
                            attemptsToPublish++
                        }
                        else
                        {
                            showError()
                        }
                    }
                )
        )
        disposeBag.add(
            model.doLedStatusRequest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        attemptsToPublish = 0
                    },
                    {
                        if (attemptsToPublish <= 3)
                        {
                            doRequests()
                            attemptsToPublish++
                        }
                        else
                        {
                            showError()
                        }
                    }
                )
        )
    }

//    private fun createSubscribers()
//    {
//        disposeBag.add(
//            model.dataSource()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    {
//                        if (it.first == FROM_LED_TOPIC)
//                        {
//                            ledStatusReceived = true
//                            ledStatus = it.second == "true"
//                            view!!.showLedStatus(ledStatus)
//                            if (ledStatusWaiting)
//                            {
//                                view!!.changeChipEnabled(true)
//                                ledStatusWaiting = false
//                            }
//                            checkIsReady()
//                        }
//                        else if (it.first == FROM_TIME_TOPIC)
//                        {
//                            timeReceived = true
//                            view!!.showTime("$it мин.")
//                            checkIsReady()
//                        }
//                    },
//                    {
//                        showError()
//                    }
//                )
//        )
//    }

    private fun createSubscribers()
    {
        disposeBag.add(
            model.timeDataSource()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        timeReceived = true
                        view!!.showTime("$it мин.")
                        checkIsReady()
                    },
                    {
                        showError()
                    }
                )
        )
        disposeBag.add(
            model.ledStatusDataSource()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        ledStatusReceived = true
                        view!!.showLedStatus(it)
                        if (ledStatusWaiting)
                        {
                            view!!.changeChipEnabled(true)
                            ledStatusWaiting = false
                        }
                        checkIsReady()
                    },
                    {
                        showError()
                    }
                )
        )
    }

    private fun checkIsReady()
    {
        if (timeReceived && ledStatusReceived)
        {
            view!!.changeConnectionStatus(2)
        }
    }

    private fun showError()
    {
        timeReceived = false
        ledStatusReceived = false
        attemptsToConnect = 0
        attemptsToSubscribe = 0
        attemptsToPublish = 0
        view!!.changeConnectionStatus(0)
    }

    fun detachView()
    {
        view = null
        disposeBag.dispose()
        model.kill()
    }
}
