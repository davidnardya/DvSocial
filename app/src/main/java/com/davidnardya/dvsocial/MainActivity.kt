package com.davidnardya.dvsocial

import android.os.Bundle
import android.util.Log
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
import com.davidnardya.dvsocial.events.UserEvents
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.navigation.SetupNavGraph
import com.davidnardya.dvsocial.navigation.screens.Screen
import com.davidnardya.dvsocial.utils.UserAuthenticator
import com.davidnardya.dvsocial.viewmodel.LoginViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var feedViewModel: FeedViewModel

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var userAuthenticator: UserAuthenticator

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        initObservers()

        lifecycleScope.launch {
            loginViewModel.getEventsFlow().collect {
                userAuthenticator.handleUserEvent(it)
            }
        }

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
                    loginViewModel = loginViewModel
                )
            }
        }
    }

    private fun initObservers() {
        loginViewModel.currentUser.observe(this) {
            if (
                it != null &&
                it.username?.isNotEmpty() == true && it.password?.isNotEmpty() == true &&
                it.username != "null" && it.password != "null"
            ) {
                Log.d("123321","it.username ${it.username}")
                lifecycleScope.launch {
                    if(loginViewModel.getUserLoggedIn()) {
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