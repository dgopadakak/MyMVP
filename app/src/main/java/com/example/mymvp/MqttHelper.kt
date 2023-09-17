package com.example.mymvp

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttException

class MqttHelper(context: Context)
{
    private val mqttAndroidClient: MqttAndroidClient
    init
    {
        mqttAndroidClient = MqttAndroidClient(context
            , "7cc77ccf90ea4aff8def5615d31d2b34.s1.eu.hivemq.cloud", "n1")
        try
        {
            val token = mqttAndroidClient.connect()
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken)
                {
                    Log.i("Connection", "success ")
                    //connectionStatus = true
                    // Give your callback on connection established here
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable)
                {
                    //connectionStatus = false
                    Log.i("Connection", "failure")
                    // Give your callback on connection failure here
                    exception.printStackTrace()
                }
            }
        }
        catch (e: MqttException)
        {
            // Give your callback on connection failure here
            e.printStackTrace()
        }
    }
}