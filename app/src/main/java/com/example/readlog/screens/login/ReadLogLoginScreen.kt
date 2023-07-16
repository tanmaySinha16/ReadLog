package com.example.readlog.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.readlog.R
import com.example.readlog.components.EmailInput
import com.example.readlog.components.PasswordInput
import com.example.readlog.components.ReadLogLogo
import com.example.readlog.components.SubmitButton
import com.example.readlog.navigation.ReadLogScreens

@Composable
fun ReadLogLoginScreen(
    navController: NavController,
    loginScreenViewModel: LoginScreenViewModel = viewModel()
) {

    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    Surface(modifier = Modifier.fillMaxSize().padding(top=20.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            ReadLogLogo()
            if (showLoginForm.value)
                UserForm(
                    loading = false,
                    isCreateAccount = false
                ) { email, password ->
                    Log.d("Form", "ReaderLoginScreen :$email @password :$password")
                    loginScreenViewModel.signInWithEmailAndPassword(email, password){
                        navController.navigate(ReadLogScreens.ReadLogHomeScreen.name)
                    }
                }
            else
                UserForm(
                    loading = false,
                    isCreateAccount = true
                ) { email, password ->
                    loginScreenViewModel.createUserWithEmailAndPassword(email, password){
                        navController.navigate(ReadLogScreens.ReadLogHomeScreen.name)
                    }
                }

        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val text = if (showLoginForm.value) "Sign up" else "Login"
            Text(text = "New User?")
            Text(text = text,
                modifier = Modifier
                    .clickable {
                        showLoginForm.value = !showLoginForm.value
                    }
                    .padding(start = 5.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary)
        }

    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable() {
        mutableStateOf(false)
    }
    val passwordFocusReset = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        if (isCreateAccount) Text(
            text = stringResource(id = R.string.create_acct),
            modifier = Modifier.padding(4.dp)
        ) else Text("")

        EmailInput(emailState = email, enabled = !loading,
            onAction = KeyboardActions {
                passwordFocusReset.requestFocus()
            })

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusReset),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid)
                    return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
            }

        )
        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid,
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }


    }


}





