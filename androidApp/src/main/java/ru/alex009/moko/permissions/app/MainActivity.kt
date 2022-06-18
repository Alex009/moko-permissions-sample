package ru.alex009.moko.permissions.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.icerock.moko.mvvm.getViewModel
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.alex009.moko.permissions.app.databinding.ActivityMainBinding
import ru.alex009.moko.permissions.shared.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: MainViewModel = getViewModel {
            val permissionsController = PermissionsController(
                applicationContext = applicationContext
            )
            MainViewModel(
                permissionsController = permissionsController
            )
        }
        viewModel.permissionsController.bind(lifecycle, supportFragmentManager)

        binding.actionBtn.setOnClickListener { viewModel.onButtonPressed() }
        binding.goToSettingsBtn.setOnClickListener { openAppSettings() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { bindState(binding, it) }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.onResume()
            }
        }
    }

    private fun bindState(binding: ActivityMainBinding, state: MainViewModel.State) {
        binding.outputText.text = state.message
        binding.goToSettingsBtn.visibility =
            if (state is MainViewModel.State.DeniedAlways) View.VISIBLE else View.GONE
        binding.actionBtn.isEnabled = state !is MainViewModel.State.DeniedAlways
    }

    private fun openAppSettings() {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}
