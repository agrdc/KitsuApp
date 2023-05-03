package com.example.kitsuapp.ui.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.example.kitsuapp.domain.ValidatePasswordUseCase
import com.example.kitsuapp.domain.usecase.ValidateEmailUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : SingleEventViewModel<LoginEvent>() {

    private val _stateFlow = MutableStateFlow(LoginViewState())
    val stateFlow = _stateFlow.asStateFlow()

    fun validateEmailState(emailTextFieldValue: TextFieldValue) {
        _stateFlow.update {
            it.copy(
                emailTextFieldValue = emailTextFieldValue,
                loading = false, emailValidationError =
                if (validateEmailUseCase(emailTextFieldValue.text)) "" else "Invalid e-mail"
            )
        }
    }

    fun validatePasswordState(passwordTextFieldValue: TextFieldValue) {
        _stateFlow.update {
            it.copy(
                passwordTextFieldValue = passwordTextFieldValue,
                loading = false, passwordValidationError =
                if (validatePasswordUseCase(passwordTextFieldValue.text)) "" else "Password should contain at least 6 characters",
            )
        }
    }

    fun validateButtonState() {
        _stateFlow.update {
            it.copy(
                buttonEnabled = validatePasswordUseCase(it.passwordTextFieldValue.text) && validateEmailUseCase(it.emailTextFieldValue.text)
            )
        }
    }

    fun toggleDialogState() {
        _stateFlow.update {
            it.copy(
                showDialog = !it.showDialog
            )
        }
    }

    fun onButtonClick() {
        viewModelScope.launch {
            _stateFlow.update {
                it.copy(loading = true)
            }
            delay(1500L)
            _stateFlow.update {
                it.copy(loading = false, showDialog = true)
            }
        }
    }
}

data class LoginViewState(
    var loading: Boolean = false,
    var emailValidationError: String = "",
    var passwordValidationError: String = "",
    var buttonEnabled: Boolean = false,
    var showDialog: Boolean = false,
    var showError: Boolean = false,
    var emailTextFieldValue: TextFieldValue = TextFieldValue(),
    var passwordTextFieldValue: TextFieldValue = TextFieldValue()
) {
    val hasPasswordValidationError get() = passwordValidationError.isNotEmpty()
    val hasEmailValidationError get() = emailValidationError.isNotEmpty()
}

sealed class LoginEvent {
    class NavigateTo(var route: String) : LoginEvent()
}