package com.example.mymvp

import android.content.Context
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttHelper(context: Context)
{
    companion object
    {
        const val SERVER_URI = "ssl://7cc77ccf90ea4aff8def5615d31d2b34.s1.eu.hivemq.cloud:8883"
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
                //mqttConnectOptions.sslProperties = com.ibm.ssl.protocol.SSL

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
                        subscriber.onComplete()
                        // Give your callback on Subscription here
                    }
                    override fun onFailure(asyncActionToken: IMqttToken, e: Throwable)
                    {
                        subscriber.onError(e)
                        // Give your subscription failure callback here
                    }
                })
            } catch (e: MqttException)
            {
                subscriber.onError(e)
                // Give your subscription failure callback here
            }
        }
    }

    fun receiveMessages(): Observable<String>
    {
        return Observable.create { subscriber ->
            mqttAndroidClient.setCallback(object : MqttCallback
            {
                override fun connectionLost(cause: Throwable)
                {
                    connectionStatus = false
                    subscriber.onComplete()     // TODO: onComplete или onError?
                }
                override fun messageArrived(topic: String, message: MqttMessage)
                {
                    try
                    {
                        val data = String(message.payload, charset("UTF-8"))
                        subscriber.onNext(data)
                        // data is the desired received message
                        // Give your callback on message received here
                    }
                    catch (e: Exception)
                    {
                        subscriber.onError(e)
                    }
                }
                override fun deliveryComplete(token: IMqttDeliveryToken)
                {
                    // Acknowledgement on delivery complete
                    TODO()          // TODO: onComplete или onError?
                }
            })
        }
    }
}