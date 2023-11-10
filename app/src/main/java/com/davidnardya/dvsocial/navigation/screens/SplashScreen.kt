package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    feedViewModel: FeedViewModel? = null,
    loginViewModel: LoginViewModel? = null,
    navController: NavHostController? = null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }

    LaunchedEffect(key1 = true) {
        var count = 0
        var i = true

        while (i && count < 5) {
            delay(1000)
            if (loginViewModel?.getUserLoggedIn() == true) {
                i = false
            }
            loginViewModel?.setCurrentUser()
            count++

        }
        if (i && count <= 5) {
            navController?.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        }
        feedViewModel?.checkFeedPostList()
    }
}