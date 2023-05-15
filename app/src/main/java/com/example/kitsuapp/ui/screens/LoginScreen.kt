package com.example.kitsuapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kitsuapp.ui.components.CustomMessageDialog
import com.example.kitsuapp.ui.viewmodel.LoginEvent
import com.example.kitsuapp.ui.viewmodel.LoginViewModel
import com.example.kitsuapp.ui.viewmodel.LoginViewState
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = getViewModel()) {
    val stateWithLifecycle = viewModel.stateFlow.collectAsStateWithLifecycle()
    val inPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    LaunchedEffect(Unit) {
        viewModel.singleEventFlow.collect { event ->
            when (event) {
                is LoginEvent.NavigateTo -> {
                    //TODO
                }
            }
        }
    }

    LoginContent(
        stateWithLifecycle.value,
        viewModel::validateEmailState,
        viewModel::validatePasswordState,
        viewModel::validateButtonState,
        viewModel::toggleDialogState,
        viewModel::onButtonClick,
        inPortrait
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    state: LoginViewState,
    validateEmail: ((email: TextFieldValue) -> Unit)? = null,
    validatePassword: ((password: TextFieldValue) -> Unit)? = null,
    validateButtonState: (() -> Unit)? = null,
    toggleDialogState: (() -> Unit)? = null,
    onButtonClick: (() -> Unit)? = null,
    inPortrait: Boolean = true,
) {

    var showSnackbar by remember {
        mutableStateOf(true)
    }

//    LaunchedEffect(key1 = showSnackbar) {
//        if (showSnackbar) {
//            delay(2000)
//            showSnackbar = false
//        }
//    }


    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (emailField, passwordField, button, snackBar) = createRefs()
        val verticalGuideline = createGuidelineFromTop(0.35F.takeIf { inPortrait } ?: 0.25F)
        val horizontalGuidelineStart = createGuidelineFromStart(fraction = 0.1f)
        val horizontalGuidelineEnd = createGuidelineFromEnd(fraction = 0.1f)
        val snackBarGuidelineStart = createGuidelineFromStart(fraction = 0.01f.takeIf { inPortrait } ?: 0.001f)
        val snackBarGuidelineEnd = createGuidelineFromEnd(fraction = 0.01f.takeIf { inPortrait } ?: 0.001f)
        val buttonGuideline = createGuidelineFromBottom(fraction = 0.075f)

        OutlinedTextField(shape = RoundedCornerShape(8.dp),
            modifier = Modifier.constrainAs(emailField) {
                top.linkTo(verticalGuideline)
                start.linkTo(horizontalGuidelineStart)
                end.linkTo(horizontalGuidelineEnd)
                width = Dimension.fillToConstraints
            },
            singleLine = true,
            label = { Text(text = "E-mail") },
            value = state.emailTextFieldValue,
            onValueChange = {
                validateEmail?.invoke(it)
                validateButtonState?.invoke()
            },
            isError = state.hasEmailValidationError,
            supportingText = {
                if (state.hasEmailValidationError) {
                    Text(state.emailValidationError)
                }
            })

        OutlinedTextField(shape = RoundedCornerShape(8.dp),
            modifier = Modifier.constrainAs(passwordField) {
                top.linkTo(emailField.bottom, 8.dp)
                start.linkTo(horizontalGuidelineStart)
                end.linkTo(horizontalGuidelineEnd)
                width = Dimension.fillToConstraints
            },
            singleLine = true,
            label = { Text(text = "Password") },
            value = state.passwordTextFieldValue,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                validatePassword?.invoke(it)
                validateButtonState?.invoke()
            },
            isError = state.hasPasswordValidationError,
            supportingText = {
                if (state.hasPasswordValidationError) {
                    Text(state.passwordValidationError)
                }
            })



        Button(shape = RoundedCornerShape(8.dp), modifier = Modifier
            .constrainAs(button) {
                start.linkTo(horizontalGuidelineStart)
                end.linkTo(horizontalGuidelineEnd)
                linkTo(passwordField.bottom, buttonGuideline, bias = 1F)
                width = Dimension.fillToConstraints
            }
            .height(60.dp), enabled = state.buttonEnabled, onClick = { onButtonClick?.invoke() }) {
            if (state.loading) {
                CircularProgressIndicator(modifier = Modifier.size(26.dp), color = Color.White)
            } else {
                Text(text = "Login")
            }
        }

        if (showSnackbar) {
            Snackbar(modifier = Modifier
                .constrainAs(snackBar) {
                    start.linkTo(snackBarGuidelineStart)
                    end.linkTo(snackBarGuidelineEnd)
                    top.linkTo(button.bottom, margin = 2.dp)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints

                },
                action = {
                    Text(text = "Action",
                        color = Color.White,
                        modifier = Modifier.clickable {
                            showSnackbar = false
                        }
                    )
                }) {
                Text("Message")
            }
        }
    }

    if (state.showDialog) {
        CustomMessageDialog(
            outlinedButtonText = "Simulate error",
            filledButtonText = "Proceed",
            outlinedButtonAction = {
                toggleDialogState?.invoke()
                showSnackbar = true
            },
            filledButtonAction = {
                //TODO navigate to next screen
            },
            //TODO move to strings.xml
            title = "Information",
            message = "This \"login\" is only symbolic and no data is being sent or stored anywhere. I created this just to show how field validations would fit in this app's architecture.",
        )
    }
}

@Preview
@Composable
fun LoginContentPreview() {
    LoginContent(state = LoginViewState(loading = true, buttonEnabled = true))
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun LoginContentPreviewLandscape() {
    LoginContent(state = LoginViewState(loading = true, buttonEnabled = true))
}