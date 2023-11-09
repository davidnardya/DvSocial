package com.davidnardya.dvsocial.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.navigation.screens.*
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    feedPostList: List<UserPost>,
    feedViewModel: FeedViewModel,
    loginViewModel: LoginViewModel
) {
//    val user by loginViewModel.currentUser.observeAsState()

    LaunchedEffect(loginViewModel.currentUser.value != null) {
        var i = true
        while(i) {
            delay(1000)
            loginViewModel.setCurrentUser()
            Log.d("123321","loginViewModel.currentUser.value ${loginViewModel.currentUser.value}")
            if(loginViewModel.currentUser.value != null) {
                i = false
            }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination =
//        if(user != null) {
//            Screen.Splash.route
//        } else {
            Screen.Login.route
//        }
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
            SplashScreen(feedViewModel)
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

//    LaunchedEffect(key1 = true) {
//        delay(5000)
//        if(user != null && user?.id != "") {
//            navHostController.navigate(Screen.Splash.route) {
//                popUpTo(Screen.Login.route) {
//                    inclusive = true
//                }
//            }
//        }
//    }

}