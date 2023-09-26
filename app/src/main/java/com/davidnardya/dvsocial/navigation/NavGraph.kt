package com.davidnardya.dvsocial.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.navigation.screens.*
import com.davidnardya.dvsocial.viewmodel.ChatViewModel
import com.davidnardya.dvsocial.viewmodel.FeedViewModel

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    feedPostList: List<UserPost>,
    feedViewModel: FeedViewModel,
    chatViewModel: ChatViewModel,
    context: Context
) {
    val user = feedViewModel.getCurrentUser()
    NavHost(
        navController = navHostController,
        startDestination =
        if (
            user?.userName != null &&
            user.userName != "" && user.password != "" &&
            user.userName != "null" && user.password != "null"
        ) {
            Screen.Splash.route
        } else {
            Screen.Login.route
        }
    ) {
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navHostController, feedViewModel)
        }
        composable(
            route = Screen.Registration.route
        ) {
            RegistrationScreen(navHostController, feedViewModel)
        }
        composable(
            route = Screen.Feed.route
        ) {
            FeedScreen(feedPostList, navHostController, feedViewModel)
        }
        composable(
            route = Screen.Splash.route
        ) {
            SplashScreen(feedViewModel)
        }
        composable(
            route = Screen.Chat.route
        ) {
            ChatScreen(navHostController, chatViewModel)
        }
        composable(
            route = Screen.ChatChannel.route
        ) {
            ChatChannelScreen(chatViewModel)
        }
        composable(
            route = Screen.PostComments.route
        ) {
            PostCommentsScreen(feedViewModel, navHostController)
        }
        composable(
            route = Screen.UserProfile.route
        ) {
            UserProfileScreen(feedViewModel, navHostController)
        }
        composable(
            route = Screen.Notifications.route
        ) {
            NotificationsScreen(feedViewModel, navHostController)
        }
        composable(
            route = Screen.Post.route
        ) {
            PostScreen()
        }
        composable(
            route = Screen.Comment.route
        ) {
            CommentScreen()
        }
    }
}