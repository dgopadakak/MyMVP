package com.example.mymvp

import android.content.Context
import io.reactivex.rxjava3.core.Completable
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException

class MqttHelper(context: Context)
{
    companion object
    {
        const val SERVER_URI = "7cc77ccf90ea4aff8def5615d31d2b34.s1.eu.hivemq.cloud"
        const val USERNAME = "dgopadakak"
        const val PASSWD = "Ww12345678910"
        const val CLIENT_ID = "phone"
        const val TOPIC = "home"
        const val QOS = 2
    }

    private var mqttAndroidClient: MqttAndroidClient = MqttAndroidClient(context
        , SERVER_URI, CLIENT_ID)
    var connectionStatus = false

    fun connect(): Completable
    {
        return Completable.create { subscriber ->
            try
            {
                val mqttConnectOptions = MqttConnectOptions()
                mqttConnectOptions.userName = USERNAME
                mqttConnectOptions.password = PASSWD.toCharArray()

                val token = mqttAndroidClient.connect(mqttConnectOptions)
                token.actionCallback = object : IMqttActionListener
                {
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

    fun subscribeTopic(): Completable
    {
        return Completable.create { subscriber ->
            try
            {
                mqttAndroidClient.subscribe(TOPIC, QOS, null, object : IMqttActionListener
                {
                    override fun onSuccess(asyncActionToken: IMqttToken)
                    {
                        TODO()
                        // Give your callback on Subscription here
                    }
                    override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable)
                    {
                        TODO()
                        // Give your subscription failure callback here
                    }
                })
            } catch (e: MqttException)
            {
                TODO()
                // Give your subscription failure callback here
            }
        }
    }
}