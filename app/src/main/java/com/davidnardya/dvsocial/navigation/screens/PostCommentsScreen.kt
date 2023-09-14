package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.utils.showLikesText
import com.davidnardya.dvsocial.viewmodel.FeedViewModel

@Composable
fun PostCommentsScreen(viewModel: FeedViewModel, navController: NavHostController) {
    val commentsList = viewModel.currentPost.value?.comments

    commentsList?.let { list ->
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(list) { index, comment ->
                var likes by rememberSaveable {
                    mutableStateOf(comment.likes ?: 0)
                }
                if(index == 0) {
                    SetHeader(likeable = comment, navController = navController)
                }

                Text(
                    modifier = Modifier.padding(6.dp),
                    text = comment.text ?: ""
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    CreateLikeButton(comment, likes, onLikesChange = { likes = it })
                    Text(
                        text = showLikesText(likes),
                        modifier = Modifier
                            .padding(6.dp)
                    )
                }

            }
        }
    } ?: run {
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