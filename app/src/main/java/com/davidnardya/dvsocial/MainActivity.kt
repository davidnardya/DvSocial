package com.davidnardya.dvsocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.utils.Constants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowFeed()
        }
    }

    @Composable
    fun ShowFeed() {
        val users = listOf(
            Constants.userI,
            Constants.userII,
            Constants.userIII
        )
        val posts = mutableListOf<UserPost>()
        users.forEach { user ->
            user.posts.forEach { post ->
                post.userName = user.userName
                posts.add(post)
            }
        }
        LazyColumn {
            items(posts) { post ->
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Row {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(post.imageUrl)
                                    .build(),
                                contentDescription = post.caption,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(post.userName)
                                    }
                                    append(": ${post.caption}")
                                },
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}