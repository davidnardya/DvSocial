package com.davidnardya.dvsocial.navigation.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.InsertComment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.navigation.AppBar
import com.davidnardya.dvsocial.navigation.navdrawer.DrawerBody
import com.davidnardya.dvsocial.navigation.navdrawer.DrawerHeader
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.utils.showToast
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    postList: List<UserPost>,
    navController: NavHostController,
    viewModel: FeedViewModel
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                onNavigationItemClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerHeader()
            DrawerBody(
                items = Constants.menuList,
                onItemClick = {
                    when (it.id) {
                        Constants.FEED_ID -> {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }

                        Constants.CHAT_ID -> {
                            navController.navigate(route = Screen.Chat.route)
                        }

                        Constants.LOGOUT_ID -> {
                            viewModel.userLogOut()
                            navController.navigate(route = Screen.Login.route) {
                                popUpTo(Screen.Feed.route) {
                                    inclusive = true
                                }
                                popUpTo(Screen.Feed.route) {
                                    inclusive = true
                                }
                            }
                        }

                        else -> {
                            showToast("${it.title} - Coming soon!")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            Modifier.padding(paddingValues)
            PopulateFeedContent(postList, navController, viewModel)
        }
    )
}

@Composable
private fun PopulateFeedContent(postList: List<UserPost>, navController: NavHostController, viewModel: FeedViewModel) {
    LazyColumn {
        items(postList) { post ->

            var likes by rememberSaveable {
                mutableStateOf(post.likes)
            }
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr
            ) {
                Row {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
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
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CreateLikeButton(post, likes, onLikesChange = { likes = it })
                            IconButton(
                                onClick = {
                                    navController.navigate(route = Screen.PostComments.route)
                                    viewModel.currentPost.value = post
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Comment,
                                    contentDescription = "${post.postId} Comments Icon"
                                )
                            }
                            IconButton(
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "${post.postId} Share Icon"
                                )
                            }
                        }
                        Text(
                            text = "$likes likes",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreateLikeButton(post: UserPost, likes: Int, onLikesChange: (Int) -> Unit) {
    var icon by rememberSaveable {
        mutableStateOf(post.isLiked)
    }
    IconButton(
        onClick = {
            icon = !icon
            post.isLiked = icon
            if (post.isLiked) {
                onLikesChange(likes.plus(1))
                post.likes = likes
            } else {
                onLikesChange(likes.minus(1))
                post.likes = likes
            }
        }
    ) {
        Icon(
            imageVector = if (icon) Icons.Filled.ThumbUp else Icons.Filled.ThumbUpOffAlt,
            contentDescription = "${post.postId} Likes Icon"
        )
    }
}