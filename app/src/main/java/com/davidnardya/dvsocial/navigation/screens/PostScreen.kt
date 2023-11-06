package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.davidnardya.dvsocial.R
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel

@Composable
fun PostScreen(
    navController: NavHostController,
    feedViewModel: FeedViewModel,
    loginViewModel: LoginViewModel
) {
    Column {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(Screen.Camera.route)
            }
        ) {
            Text(text = "Add a new picture")
        }
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Show Image"
        )
    }

}