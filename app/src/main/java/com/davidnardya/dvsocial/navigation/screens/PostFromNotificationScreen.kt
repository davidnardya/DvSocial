package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.davidnardya.dvsocial.utils.showLikesText
import com.davidnardya.dvsocial.viewmodel.FeedViewModel

@Composable
fun PostFromNotificationScreen(feedViewModel: FeedViewModel, navHostController: NavHostController) {
    val post = feedViewModel.currentPostState.value
    var likes by rememberSaveable {
        mutableIntStateOf(post.likes?.size ?: 0)
    }
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.imageUrl)
                    .build(),
                contentDescription = "Username",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Black, CircleShape)
            )
            Text(
                text = post.username ?: "",
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding(start = 6.dp)
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.imageUrl)
                .build(),
            contentDescription = post.caption,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(post.username ?: "")
                }
                append(": ${post.caption}")
            },
            modifier = Modifier.padding(10.dp)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CreateLikeButton(
                post,
                likes,
                onLikesChange = { likes = it },
                feedViewModel = feedViewModel
            )
            Text(
                text = showLikesText(likes),
                modifier = Modifier
                    .padding(6.dp)
            )
        }
        PostCommentsScreen(viewModel = feedViewModel, navController = navHostController)
    }
}