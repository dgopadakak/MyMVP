package com.example.mymvp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymvp.presenter.Presenter
import com.example.mymvp.R
import com.example.mymvp.appComponent
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
