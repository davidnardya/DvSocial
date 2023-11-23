package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.utils.showToast
import com.davidnardya.dvsocial.utils.userLoginAuthProduceResult
import com.davidnardya.dvsocial.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navHostController: NavHostController, viewModel: LoginViewModel) {
    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val loginFailures = rememberSaveable { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = userName,
                label = { Text(text = "Username") },
                onValueChange = { userName = it },
                placeholder = { Text(text = "Enter your username") },
                textStyle = handleLoginFailure(loginFailures.value)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = password,
                label = { Text(text = "Password") },
                onValueChange = { password = it },
                placeholder = { Text(text = "Enter your password") },
                textStyle = handleLoginFailure(loginFailures.value)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    scope.launch {
                        viewModel.userAttemptLogin(userName, password)
                        if (userLoginAuthProduceResult.receive()) {
                            navHostController.navigate(route = Screen.Splash.route) {
                                popUpTo(Screen.Login.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            loginFailures.value = loginFailures.value.plus(1)
                        }
                    }
                }
            ) {
                Text(text = "Log in")
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navHostController.navigate(route = Screen.Registration.route)
                }
            ) {
                Text(text = "Don't have a user yet? Register now!")
            }
        }
    }

}

@Composable
fun handleLoginFailure(loginFailures: Int): TextStyle {
    return if (loginFailures in 1..3) {
        TextStyle(color = Color.Red)
    } else if (loginFailures > 3) {
        showToast("Wrong username or password")
        TextStyle(color = Color.Black)
    } else {
        TextStyle(color = Color.Unspecified)
    }
}