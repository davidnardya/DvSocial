package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel

@Composable
fun PostScreen(
    navController: NavHostController,
    feedViewModel: FeedViewModel,
    loginViewModel: LoginViewModel
) {
    var postText by rememberSaveable { mutableStateOf("") }
    var buttonHeight by rememberSaveable { mutableStateOf(0) }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Column(
        modifier = Modifier.padding(6.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(Screen.Camera.route)
            }
        ) {
            Text(text = "Add a new picture")
        }
        TextField(
            modifier = Modifier
                .heightIn(0.dp, (screenHeight - buttonHeight).dp)
                .fillMaxWidth(),
            value = postText,
            placeholder = { Text(text = "Write something here")},
            onValueChange = {
                if(it.length <= Constants.MAX_COMMENT_LENGTH) {
                    postText = it
                }
            }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    buttonHeight = it.size.height
                },
            onClick = {
                //Add new post to user's post list
            }
        ) {
            Text(text = "POST")
        }
    }
}