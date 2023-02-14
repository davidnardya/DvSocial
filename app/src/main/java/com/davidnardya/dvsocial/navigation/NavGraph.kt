package com.davidnardya.dvsocial.navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.navigation.screens.FeedScreen
import com.davidnardya.dvsocial.navigation.screens.LoginScreen
import com.davidnardya.dvsocial.navigation.screens.RegistrationScreen
import com.davidnardya.dvsocial.navigation.screens.Screen
import com.davidnardya.dvsocial.utils.Constants
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
        if(
            user?.userName != "" && user?.password != "" ||
            user.userName != "null" && user.password != "null"
        ) {
            Screen.Feed.route
        } else {
            Screen.Login.route
        }
    ) {
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navHostController, viewModel, context)
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
    }
}