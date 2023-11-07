package com.davidnardya.dvsocial.navigation.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun PostScreen(
    navController: NavHostController,
    feedViewModel: FeedViewModel,
    loginViewModel: LoginViewModel,
    uri: Uri?
) {
    val scope = rememberCoroutineScope()
    var postText by rememberSaveable { mutableStateOf("") }
    var buttonHeight by rememberSaveable { mutableStateOf(0) }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Column(
        modifier = Modifier.padding(6.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    buttonHeight = it.size.height
                },
            onClick = {
                //Add new post to user's post list
                if (uri != null) {
                    val path = loginViewModel.uploadImage(uri)
                    scope.launch {
                        val url = loginViewModel.getImageDownloadUrl(path)
                        Log.d("123321","url: $url")
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
            placeholder = { Text(text = "Write something here")},
            onValueChange = {
                if(it.length <= Constants.MAX_COMMENT_LENGTH) {
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
        if(selectedImageUri?.path != null && selectedImageUri?.path != "") {
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