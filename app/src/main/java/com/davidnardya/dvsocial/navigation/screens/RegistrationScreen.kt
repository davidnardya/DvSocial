package com.davidnardya.dvsocial.navigation.screens

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
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.utils.showToast
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel

@Composable
fun RegistrationScreen(
    navHostController: NavHostController,
    viewModel: LoginViewModel
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

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
                onValueChange = { userName = it},
                placeholder = { Text(text = "Enter your username") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = password,
                label = { Text(text = "Password") },
                onValueChange = { password = it},
                placeholder = { Text(text = "Enter your password") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    viewModel.registerUser(userName, password)
                    showToast("User created!")
                    navHostController.navigate(route = Screen.Login.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Text(text = "Register")
            }
        }
    }
}