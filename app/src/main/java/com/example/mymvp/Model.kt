package com.example.mymvp

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class Model(val mqttHelper: MqttHelper)
{
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
}
