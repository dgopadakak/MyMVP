package com.example.mymvp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mymvp.appComponent
import com.example.mymvp.databinding.ActivityMainBinding
import com.example.mymvp.presenter.Presenter
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Viewable
{
    @Inject
    lateinit var presenter: Presenter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        appComponent.inject(this)

        binding.ledChip.setOnClickListener { presenter.ledChipClicked() }

        presenter.attachView(this)
    }

    override fun showTime(time: String)
    {
        binding.timeValTextView.text = time
    }

    override fun showLedStatus(status: Boolean)
    {
        binding.ledChip.isChecked = status
    }

    override fun changeChipEnabled(status: Boolean)
    {
        binding.ledChip.isEnabled = status
    }

    /**
     * Данный метод отвечает за перевод отображения в режим работы или загрузки
     */
    override fun changeConnectionStatus(status: Boolean)
    {
        if (status)
        {
            binding.progressBar.visibility = View.INVISIBLE
        }
        else
        {
            binding.progressBar.visibility = View.VISIBLE
        }
        binding.timeTitleTextView.isEnabled = status
        binding.timeValTextView.isEnabled = status
        binding.ledChip.isEnabled = status
    }

    override fun onDestroy()
    {
        super.onDestroy()
        presenter.detachView()
    }
}
