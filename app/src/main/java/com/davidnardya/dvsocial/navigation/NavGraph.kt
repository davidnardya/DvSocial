package com.davidnardya.dvsocial.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.navigation.screens.FeedScreen
import com.davidnardya.dvsocial.navigation.screens.LoginScreen
import com.davidnardya.dvsocial.navigation.screens.RegistrationScreen
import com.davidnardya.dvsocial.navigation.screens.Screen

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    feedPostList: List<UserPost>,

    ) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navHostController)
        }
        composable(
            route = Screen.Registration.route
        ) {
            RegistrationScreen(navHostController)
        }
        composable(
            route = Screen.Feed.route
        ) {
            FeedScreen(feedPostList)
        }
    }
}