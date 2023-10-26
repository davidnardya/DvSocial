package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import com.davidnardya.dvsocial.model.Likeable
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.navigation.AppBar
import com.davidnardya.dvsocial.navigation.navdrawer.DrawerBody
import com.davidnardya.dvsocial.navigation.navdrawer.DrawerHeader
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.utils.showLikesText
import com.davidnardya.dvsocial.utils.showToast
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    postList: List<UserPost>,
    navController: NavHostController,
    feedViewModel: FeedViewModel,
    loginViewModel: LoginViewModel
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
            SetDrawerBody(navController, scope, scaffoldState, loginViewModel)
        },
        content = { paddingValues ->
            Modifier.padding(paddingValues)
            PopulateFeedContent(postList, navController, feedViewModel)
        }
    )
}

@Composable
fun PopulateFeedContent(
    postList: List<UserPost>,
    navController: NavHostController,
    viewModel: FeedViewModel,
) {
    LazyColumn {
        itemsIndexed(postList) { index, post ->
            var likes by rememberSaveable {
                mutableStateOf(post.likes ?: 0)
            }
            Column {
                if (index == 0) {
                    SetHeader(post, navController)
                }
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
                    CreateLikeButton(post, likes, onLikesChange = { likes = it })
                    IconButton(
                        onClick = {
                            navController.navigate(route = Screen.PostComments.route)
                            viewModel.currentPostState.value = post
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Comment,
                            contentDescription = "${post.username} Comments Icon"
                        )
                    }
                    IconButton(
                        onClick = { showToast("Share coming soon!") }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "${post.username} Share Icon"
                        )
                    }
                }
                Text(
                    text = showLikesText(likes),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                )
            }
        }
    }
}

@Composable
fun CreateLikeButton(likeable: Likeable, likes: Int, onLikesChange: (Int) -> Unit) {
    var icon by rememberSaveable {
        mutableStateOf(likeable.isLiked ?: false)
    }
    IconButton(
        onClick = {
            icon = !icon
            likeable.isLiked = icon
            if (likeable.isLiked == true) {
                onLikesChange(likes.plus(1))
                likeable.likes = likes
            } else {
                onLikesChange(likes.minus(1))
                likeable.likes = likes
            }
        }
    ) {
        Icon(
            imageVector = if (icon) Icons.Filled.ThumbUp else Icons.Filled.ThumbUpOffAlt,
            contentDescription = "Likes Icon"
        )
    }
}

@Composable
fun SetHeader(likeable: Likeable, navController: NavHostController) {
    IconButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        onClick = {
            navController.navigate(
                if (likeable is UserPost) {
                    Screen.Post.route
                } else {
                    Screen.Comment.route
                }
            )
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AddBox,
                contentDescription = "Feed Header"
            )
            Text(
                text = if (likeable is UserPost) "Create a new post" else "Add a new comment",
                modifier = Modifier
                    .padding(6.dp)
            )
        }
    }
}

@Composable
private fun SetDrawerBody(
    navController: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    viewModel: LoginViewModel
) {
    DrawerBody(
        items = Constants.menuList,
        onItemClick = {
            when (it.id) {
                Constants.USER_PROFILE_ID -> {
                    navController.navigate(Screen.UserProfile.route)
                }

                Constants.NOTIFICATIONS_ID -> {
                    navController.navigate(Screen.Notifications.route)
                }

                Constants.FEED_ID -> {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
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
}