package com.example.mymvp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymvp.R
import com.example.mymvp.appComponent
import com.example.mymvp.databinding.ActivityMainBinding
import com.example.mymvp.presenter.Presenter
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Viewable
{
    @Inject
    lateinit var presenter: Presenter
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        appComponent.inject(this)

        binding.ledSwitch.setOnCheckedChangeListener { _, _ ->
            presenter.ledSwitchCheckedChange()
        }

        presenter.attachView(this)
    }

    override fun showTime(time: String)
    {
        binding.timeValTextView.text = time
    }

    override fun showLedStatus(status: Boolean)
    {
        binding.ledSwitch.isChecked = status
    }

    override fun changeSwitchEnabled(status: Boolean)
    {
        binding.ledSwitch.isEnabled = status
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
                binding.ledSwitch.isEnabled = false
            }
            1 -> {  // Подключение
                binding.progressBar.visibility = View.VISIBLE
                binding.timeTitleTextView.isEnabled = false
                binding.timeValTextView.isEnabled = false
                binding.ledSwitch.isEnabled = false
            }
            2 -> {  // Подключено
                binding.progressBar.visibility = View.INVISIBLE
                binding.timeTitleTextView.isEnabled = true
                binding.timeValTextView.isEnabled = true
                binding.ledSwitch.isEnabled = true
            }
        }
    }

    override fun makeToast(publishDone: Boolean)
    {
        val text = if (publishDone) getString(R.string.publish_done)
            else getString(R.string.publish_error)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        presenter.detachView()
    }
}
