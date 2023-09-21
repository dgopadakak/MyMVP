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
    override fun changeConnectionStatus(statusNum: Int)
    {
        when (statusNum)
        {
            0 -> {  // Ошибка
                // TODO()
                binding.progressBar.visibility = View.INVISIBLE
                binding.timeTitleTextView.isEnabled = false
                binding.timeValTextView.isEnabled = false
                binding.ledChip.isEnabled = false
            }
            1 -> {  // Подключение
                binding.progressBar.visibility = View.VISIBLE
                binding.timeTitleTextView.isEnabled = false
                binding.timeValTextView.isEnabled = false
                binding.ledChip.isEnabled = false
            }
            2 -> {  // Подключено
                binding.progressBar.visibility = View.INVISIBLE
                binding.timeTitleTextView.isEnabled = true
                binding.timeValTextView.isEnabled = true
                binding.ledChip.isEnabled = true
            }
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        presenter.detachView()
    }
}
