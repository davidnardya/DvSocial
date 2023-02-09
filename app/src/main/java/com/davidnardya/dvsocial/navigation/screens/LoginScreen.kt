package com.davidnardya.dvsocial.navigation.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.viewmodel.FeedViewModel

@Composable
fun LoginScreen(navHostController: NavHostController, viewModel: FeedViewModel, context: Context) {
    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val loginFailures = rememberSaveable { mutableStateOf(0) }

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
                    viewModel.userAttemptLogin(userName, password)
                    if(viewModel.getIsUserLoggedIn().value == true) {
                        navHostController.navigate(route = Screen.Feed.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                /*handleLoginClick(userName, password)*/
                }
            ) {
                Text(text = "Log in!")
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
                /*navigationState.value = ScreenOptions.SHOW_REGISTRATION*/
                }
            ) {
                Text(text = "Don't have a user yet? Register now!")
            }
        }
    }

}

@Composable
fun handleLoginFailure(loginFailures: Int) : TextStyle {
    return if(loginFailures in 1..3) {
        TextStyle(color = Color.Red)
    } else if(loginFailures > 3) {
        TextStyle(color = Color.Gray)
    } else {
        TextStyle(color = Color.Unspecified)
    }
}

//private fun handleLoginClick(userName: String, password: String) {
//    lifecycleScope.launch {
//        if(viewModel.userAttemptLogin(userName,password)) {
//            navigationState.value = ScreenOptions.SHOW_FEED
//        }
//    }
//}