package com.davidnardya.dvsocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.lifecycleScope
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.davidnardya.dvsocial.navigation.SetupNavGraph
import com.davidnardya.dvsocial.navigation.screens.Screen
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var feedViewModel: FeedViewModel

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        initObservers()

        setContent {
            navController = rememberNavController()
            val posts = remember { mutableStateListOf(feedViewModel.getFeedPostList()) }.first()

            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr
            ) {
                SetupNavGraph(
                    navHostController = navController,
                    feedPostList = posts,
                    feedViewModel = feedViewModel,
                    context = this
                )
            }
        }
    }

    private fun initObservers() {
        feedViewModel.currentUser.observe(this) {
            if (
                it != null &&
                it.username.isNotEmpty() && it.password.isNotEmpty() &&
                it.username != "null" && it.password != "null"
            ) {
                lifecycleScope.launch {
                    if(feedViewModel.getUserLoggedIn()) {
                        navController.navigate(route = Screen.Splash.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                            popUpTo(Screen.Splash.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }

        feedViewModel.isLoadingComplete.observe(this) {
            if (it) {
                navController.navigate(route = Screen.Feed.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                    popUpTo(Screen.Feed.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    private fun initActivity() {
        feedViewModel.subscribeToUserListFlow()
        feedViewModel.subscribeToCurrentUserFlow()
    }
}