package ru.alex009.moko.permissions.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.icerock.moko.mvvm.flow.binding.bindText
import dev.icerock.moko.mvvm.getViewModel
import ru.alex009.moko.permissions.app.databinding.ActivityMainBinding
import ru.alex009.moko.permissions.shared.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: MainViewModel = getViewModel { MainViewModel() }

        binding.outputText.bindText(this, viewModel.resultText)

        binding.actionBtn.setOnClickListener { viewModel.onButtonPressed() }
    }
}
