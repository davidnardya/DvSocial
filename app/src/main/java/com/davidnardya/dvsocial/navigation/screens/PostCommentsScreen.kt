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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.model.UserComment
import com.davidnardya.dvsocial.utils.showLikesText
import com.davidnardya.dvsocial.viewmodel.FeedViewModel

@Composable
fun PostCommentsScreen(viewModel: FeedViewModel, navController: NavHostController) {
    val commentsList = viewModel.currentPostState.value.comments

    if (commentsList?.size != null && commentsList.isNotEmpty()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(commentsList) { index, comment ->
                var likes by rememberSaveable {
                    mutableIntStateOf(comment.likes?.size ?: 0)
                }
                if (index == 0) {
                    SetHeader(likeable = comment, navController = navController)
                }
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = comment.username ?: "TEMP Username",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = comment.text ?: ""
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CreateLikeButton(
                        comment,
                        likes,
                        onLikesChange = { likes = it },
                        feedViewModel = viewModel
                    )
                    Text(
                        text = showLikesText(likes),
                        modifier = Modifier
                            .padding(6.dp)
                    )
                }

            }
        }
    } else {
        NoComments(navController)
    }
}

@Composable
private fun NoComments(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        SetHeader(likeable = UserComment(), navController = navController)
        Text(
            text = "No comments yet!",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}