package ru.alex009.moko.permissions.shared

import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    val permissionsController: PermissionsController
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.NotGranted)
    val state: CStateFlow<State>
        get() = _state.cStateFlow()

    fun onResume() {
        _state.value = if (permissionsController.isPermissionGranted(Permission.RECORD_AUDIO)) {
            State.Granted
        } else {
            State.NotGranted
        }
    }

    fun onButtonPressed() {
        val permissionType = Permission.RECORD_AUDIO
        requestPermission(permissionType)
    }

    private fun requestPermission(permission: Permission) {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(permission)
                _state.value = State.Granted
            } catch (deniedAlwaysException: DeniedAlwaysException) {
                _state.value = State.DeniedAlways
            } catch (deniedException: DeniedException) {
                _state.value = State.Denied
            }
        }
    }

    sealed interface State {
        val message: String

        object NotGranted : State {
            override val message: String =
                "Предоставьте приложению разрешение на работу с микрофоном"
        }

        object Granted : State {
            override val message: String = "Разрешение получено!"
        }

        object Denied : State {
            override val message: String = "Приложение не может работать без разрешения"
        }

        object DeniedAlways : State {
            override val message: String =
                "Приложение не может работать без разрешения, пожалуйста, включите его в настройках"
        }
    }
}
