package com.example.mymvp.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.mymvp.R
import com.example.mymvp.appComponent
import com.example.mymvp.presenter.Presenter
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Viewable
{
    @Inject
    lateinit var presenter: Presenter

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appComponent.inject(this)

        progressBar = findViewById(R.id.progressBar)

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

    override fun changeConnectionStatus(status: Boolean)
    {
        if (status)
        {
            setReady()
        }
        else
        {
            setLoading()
        }
    }

    private fun setReady()
    {
        progressBar.visibility = View.INVISIBLE
    }

    private fun setLoading()
    {
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
