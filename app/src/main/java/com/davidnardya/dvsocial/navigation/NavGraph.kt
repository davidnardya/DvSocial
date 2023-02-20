package com.davidnardya.dvsocial.navigation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.navigation.screens.*
import com.davidnardya.dvsocial.viewmodel.FeedViewModel

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    feedPostList: List<UserPost>,
    viewModel: FeedViewModel,
    context: Context
) {
    val user = viewModel.getCurrentUser()
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
            LoginScreen(navHostController, viewModel)
        }
        composable(
            route = Screen.Registration.route
        ) {
            RegistrationScreen(navHostController, viewModel, context)
        }
        composable(
            route = Screen.Feed.route
        ) {
            FeedScreen(feedPostList)
        }
        composable(
            route = Screen.Splash.route
        ) {
            SplashScreen(viewModel)
        }
    }
}