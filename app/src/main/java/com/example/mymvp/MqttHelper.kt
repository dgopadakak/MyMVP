package com.example.mymvp

import android.content.Context
import io.reactivex.rxjava3.core.Completable
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttException

class MqttHelper(context: Context)
{
    private var mqttAndroidClient: MqttAndroidClient = MqttAndroidClient(context
        , "7cc77ccf90ea4aff8def5615d31d2b34.s1.eu.hivemq.cloud", "n1")
    var connectionStatus = false

    fun connect(): Completable
    {
        return Completable.create { subscriber ->
            try
            {
                val token = mqttAndroidClient.connect()
                token.actionCallback = object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken)
                    {
                        connectionStatus = true
                        subscriber.onComplete()
                    }
                    override fun onFailure(asyncActionToken: IMqttToken, e: Throwable)
                    {
                        connectionStatus = false
                        subscriber.onError(e)
                    }
                }
            }
            catch (e: MqttException)
            {
                connectionStatus = false
                subscriber.onError(e)
            }
        }
    }
}