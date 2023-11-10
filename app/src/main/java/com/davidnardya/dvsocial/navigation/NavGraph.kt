package com.davidnardya.dvsocial.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.navigation.screens.*
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    feedPostList: List<UserPost>,
    feedViewModel: FeedViewModel,
    loginViewModel: LoginViewModel
) {

    NavHost(
        navController = navHostController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navHostController, loginViewModel)
        }
        composable(
            route = Screen.Registration.route
        ) {
            RegistrationScreen(navHostController, loginViewModel)
        }
        composable(
            route = Screen.Feed.route
        ) {
            FeedScreen(feedPostList, navHostController, feedViewModel, loginViewModel)
        }
        composable(
            route = Screen.Splash.route
        ) {
            SplashScreen(feedViewModel, loginViewModel, navHostController)
        }
        composable(
            route = Screen.PostComments.route
        ) {
            PostCommentsScreen(feedViewModel, navHostController)
        }
        composable(
            route = Screen.UserProfile.route
        ) {
            UserProfileScreen(feedViewModel, loginViewModel, navHostController)
        }
        composable(
            route = Screen.Notifications.route
        ) {
            NotificationsScreen(loginViewModel, navHostController)
        }
        composable(
            route = Screen.Post.route
        ) {
            PostScreen(navHostController, feedViewModel, loginViewModel, Screen.Post.uri)
        }
        composable(
            route = Screen.Comment.route
        ) {
            CommentScreen(feedViewModel, navHostController)
        }
        composable(
            route = Screen.PhotoPick.route
        ) {
            PhotoPickScreen(navHostController)
        }
    }
}