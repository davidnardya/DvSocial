package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.davidnardya.dvsocial.viewmodel.FeedViewModel

@Composable
fun PostCommentsScreen(viewModel: FeedViewModel) {
    if(
        viewModel.currentPost.value?.comments?.size == null ||
        viewModel.currentPost.value?.comments?.size == 0
        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "No comments yet!",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}