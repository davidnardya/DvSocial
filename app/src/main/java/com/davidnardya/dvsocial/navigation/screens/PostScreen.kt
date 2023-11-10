package com.davidnardya.dvsocial.navigation.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.repositories.imageDownloadUrlProduceResult
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PostScreen(
    navController: NavHostController,
    feedViewModel: FeedViewModel,
    loginViewModel: LoginViewModel,
    uri: Uri?
) {
    val currentUser by remember {
        mutableStateOf(loginViewModel.currentUser.value)
    }
    val scope = rememberCoroutineScope()
    var postText by rememberSaveable { mutableStateOf("") }
    var buttonHeight by rememberSaveable { mutableStateOf(0) }
    var showSpinner by rememberSaveable { mutableStateOf(false) }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp

    Column(
        modifier = Modifier.padding(6.dp)
    ) {
        if (showSpinner) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    buttonHeight = it.size.height
                },
            onClick = {
                //Add new post to user's post list
                if (uri != null) {
                    showSpinner = true
                    val path = loginViewModel.uploadImage(uri)
                    scope.launch {
                        delay(2000)
                        loginViewModel.getImageDownloadUrl(path)
                        delay(3000)
                        imageDownloadUrlProduceResult.tryReceive().getOrNull()?.let {
                            Log.d("123321","imageDownloadUrlProduceResult")
                            feedViewModel.uploadNewUserPost(
                                UserPost(
                                    it.toString(),
                                    postText,
                                    Constants.commentsListOne,
                                    false,
                                    0,
                                    currentUser?.username
                                ),
                                currentUser?.id,
                                loginViewModel.currentUser.value
                            )
                        }
                        delay(3000)
                        showSpinner = false
                        selectedImageUri = null
                        postText = ""
                        navController.navigate(Screen.Feed.route) {
                            popUpTo(Screen.Post.route) {
                                inclusive = true
                            }
                            popUpTo(Screen.Splash.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        ) {
            Text(text = "POST")
        }
        TextField(
            modifier = Modifier
                .heightIn(0.dp, (screenHeight - buttonHeight).dp)
                .fillMaxWidth(),
            value = postText,
            placeholder = { Text(text = "Write something here") },
            onValueChange = {
                if (it.length <= Constants.MAX_COMMENT_LENGTH) {
                    postText = it
                }
            }
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(Screen.PhotoPick.route)
            }
        ) {
            Text(text = "Add a new picture")
        }
        if (selectedImageUri?.path != null && selectedImageUri?.path != "") {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = rememberAsyncImagePainter(selectedImageUri),
                contentDescription = ""
            )
        }
    }
    selectedImageUri = uri
}