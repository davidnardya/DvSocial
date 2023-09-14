package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.viewmodel.FeedViewModel

@Composable
fun UserProfileScreen(viewModel: FeedViewModel, navController: NavHostController) {
    val user = viewModel.getCurrentUser()

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
            //Figure out this feed
        }
    }

}