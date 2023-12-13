package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.model.UserComment
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.utils.Constants.MAX_COMMENT_LENGTH
import com.davidnardya.dvsocial.utils.cleanSpaces
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CommentScreen(feedViewModel: FeedViewModel, navHostController: NavHostController) {
    var commentText by rememberSaveable { mutableStateOf("") }
    var buttonHeight by rememberSaveable { mutableStateOf(0) }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val scope = rememberCoroutineScope()
    var showSpinner by rememberSaveable { mutableStateOf(false) }
    Column {
        if (showSpinner) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
        TextField(
            modifier = Modifier
                .heightIn(0.dp, (screenHeight - buttonHeight).dp)
                .fillMaxWidth(),
            value = commentText,
            placeholder = { Text(text = "Enter your comment here")},
            onValueChange = {
                if(it.length <= MAX_COMMENT_LENGTH) {
                    commentText = it
                }
            }
        )
        IconButton(
            onClick = {
                scope.launch {
                    val currentPost = feedViewModel.currentPostState.value

                    feedViewModel.uploadNewUserComment(
                        UserComment(
                            commentText.cleanSpaces(),
                            emptyList(),
                            currentPost.username,
                            feedViewModel.generateNewId()
                        ),
                        Constants.currentUser?.id,
                        currentPost.id
                    )

//                    val newList = currentPost.comments?.toMutableList()
//                    newList?.add(
//                        UserComment(commentText.cleanSpaces(),false,0,currentPost.username)
//                    )
//                    val newPost = UserPost(
//                        imageUrl = currentPost.imageUrl,
//                        caption = currentPost.caption,
//                        comments = newList,
//                        isLiked = currentPost.isLiked,
//                        likes = currentPost.likes
//                    )
//                    feedViewModel.currentPostState.value = newPost
//                    feedViewModel.uploadNewUserPost(newPost,Constants.currentUser?.id)
                    feedViewModel.getFeedPostList()
                    showSpinner = true
                    delay(3000)
                    navHostController.navigate(Screen.PostComments.route) {
                        popUpTo(Screen.Comment.route) {
                            inclusive = true
                        }
                        popUpTo(Screen.PostComments.route) {
                            inclusive = true
                        }
                    }
                    showSpinner = false
                }
                      },
            modifier = Modifier
                .onGloballyPositioned {
                    buttonHeight = it.size.height
                }
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send Comment")
        }
    }

}
