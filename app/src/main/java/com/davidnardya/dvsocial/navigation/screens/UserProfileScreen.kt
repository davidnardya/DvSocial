package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.davidnardya.dvsocial.viewmodel.FeedViewModel

@Composable
fun UserProfileScreen(viewModel: FeedViewModel, navController: NavHostController) {
    val user = viewModel.currentUser.value

    user?.posts?.let { posts ->
        if (posts.isEmpty()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "No posts yet!")
            }
        } else {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(posts[0].imageUrl)
                        .build(),
                    contentDescription = "${user.username}Username",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                )
                Text(
                    text = "Hello, ${user.username}",
                    modifier = Modifier.padding(top = 6.dp)
                )
                PopulateFeedContent(postList = posts, navController = navController, viewModel = viewModel)
            }
        }
    }

}