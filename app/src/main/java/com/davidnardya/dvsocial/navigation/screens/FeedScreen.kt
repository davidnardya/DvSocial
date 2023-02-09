package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.davidnardya.dvsocial.model.UserPost

@Composable
fun FeedScreen(postList: List<UserPost>) {
    LazyColumn {
        items(postList) { post ->
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr
            ) {
                Row {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row (
                            Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(post.imageUrl.image)
                                    .build(),
                                contentDescription = "${post.userName}Username",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.Black, CircleShape)
                            )
                            Text(
                                text = post.userName,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                ),
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(post.imageUrl.image)
                                .build(),
                            contentDescription = post.caption,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(1f)
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