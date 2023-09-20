package com.example.mymvp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Viewable
{
    @Inject
    lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appComponent.inject(this)

        findViewById<Button>(R.id.buttonTest).setOnClickListener { presenter.sendMsg() }

        presenter.attachView(this)
    }

    override fun showTime(time: String)
    {
        TODO("Not yet implemented")
    }

    override fun showLedStatus(status: Boolean)
    {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
