package ru.alex009.moko.permissions.shared

import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    private val _resultText: MutableStateFlow<String> = MutableStateFlow("")
    val resultText: CStateFlow<String> get() = _resultText.cStateFlow()

    fun onButtonPressed() {
        _resultText.value = "hello world!"
    }
}
