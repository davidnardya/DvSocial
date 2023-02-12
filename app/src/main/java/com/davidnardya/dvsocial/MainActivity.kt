package com.davidnardya.dvsocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.davidnardya.dvsocial.model.ScreenOptions
import com.davidnardya.dvsocial.navigation.SetupNavGraph
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: FeedViewModel

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.subscribeToUserListFlow()

        setContent {
            navController = rememberNavController()
            SetupNavGraph(
                navHostController = navController,
                feedPostList = viewModel.getFeedPostList(),
                viewModel = viewModel,
                context = this
            )

        }
    }
}