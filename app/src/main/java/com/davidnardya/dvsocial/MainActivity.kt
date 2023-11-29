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
import com.davidnardya.dvsocial.repositories.UserRepository
import com.davidnardya.dvsocial.utils.UserAuthenticator
import com.davidnardya.dvsocial.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var feedViewModel: FeedViewModel

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userAuthenticator: UserAuthenticator

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        initObservers()


        lifecycleScope.launch(Dispatchers.IO) {
            loginViewModel.getEventsFlow().collect {
                userAuthenticator.handleUserEvent(it)
            }
        }

        setContent {
            navController = rememberNavController()
            feedViewModel.getFeedPostList()
            val posts = remember { mutableStateListOf(feedViewModel.postList.value) }.first()

            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr
            ) {
                if (posts != null) {
                    SetupNavGraph(
                        navHostController = navController,
                        feedPostList = posts,
                        feedViewModel = feedViewModel,
                        loginViewModel = loginViewModel
                    )
                }
            }
        }
    }

    private fun initObservers() {

        feedViewModel.isLoadingComplete.observe(this) {
            if (it) {
                navController.navigate(route = Screen.Feed.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                    popUpTo(Screen.Feed.route) {
                        inclusive = true
                    }
                    popUpTo(Screen.Post.route) {
                        inclusive = true
                    }
                }
                feedViewModel.isLoadingComplete.value = false
            }
        }
    }

    private fun initActivity() {
        feedViewModel.subscribeToUserListFlow()
        loginViewModel.subscribeToCurrentUserFlow()
    }
}