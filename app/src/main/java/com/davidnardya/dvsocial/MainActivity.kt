package com.davidnardya.dvsocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.davidnardya.dvsocial.model.ScreenOptions
import com.davidnardya.dvsocial.navigation.SetupNavGraph
import com.davidnardya.dvsocial.navigation.screens.Screen
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: FeedViewModel

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.subscribeToUserListFlow()
        viewModel.subscribeToCurrentUserFlow()

        setContent {
            navController = rememberNavController()
            val posts = remember { mutableStateListOf(viewModel.getFeedPostList()) }.first()
            SetupNavGraph(
                navHostController = navController,
                feedPostList = posts,
                viewModel = viewModel,
                context = this
            )
            
            viewModel.currentUser.observe(this) {
                if(
                    it.userName.isNotEmpty() && it.password.isNotEmpty() ||
                    it.userName != null && it.password != null ||
                    it.userName != "null" && it.password != "null"
                ) {
                    navController.navigate(route = Screen.Feed.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}