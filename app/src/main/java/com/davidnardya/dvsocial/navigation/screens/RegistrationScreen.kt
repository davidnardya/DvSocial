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

@Composable
fun RegistrationScreen(navHostController: NavHostController) {
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
                    navHostController.navigate(route = Screen.Login.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                /*handleRegistrationClick(userName, password)*/
                }
            ) {
                Text(text = "Register!")
            }
        }
    }
}

//private fun handleRegistrationClick(userName: String, password: String) {
//    viewModel.registerUser(userName, password)
//    Toast.makeText(this, "User created!", Toast.LENGTH_SHORT).show()
//    navigationState.value = ScreenOptions.SHOW_LOGIN
//
//}