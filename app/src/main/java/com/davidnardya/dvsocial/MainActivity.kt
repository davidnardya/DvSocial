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

//    private val navigationState = mutableStateOf(ScreenOptions.SHOW_LOGIN)
    private val loginFailures = mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel.getFailedLogins().observe(this) {
//            loginFailures.value = it
//        }
        viewModel.getIsUserLoggedIn().observe(this) {
            if(it) {
//                navigationState.value = ScreenOptions.SHOW_FEED
            }
        }
        viewModel.subscribeToUserListFlow()

        setContent {
            navController = rememberNavController()
            SetupNavGraph(
                navHostController = navController,
                feedPostList = viewModel.getFeedPostList(),
                viewModel = viewModel,
                context = this
            )
//            when(navigationState.value) {
//                ScreenOptions.SHOW_LOGIN -> {
//                    ShowLogin()
//                }
//                ScreenOptions.SHOW_FEED -> {
//                    ShowFeed(viewModel.getFeedPostList())
//                }
//                ScreenOptions.SHOW_REGISTRATION -> {
//                    ShowRegistration()
//                }
//            }

        }
    }

//    fun handleLoginClick(userName: String, password: String) {
//        lifecycleScope.launch {
//            if(viewModel.userAttemptLogin(userName,password)) {
//                navigationState.value = ScreenOptions.SHOW_FEED
//            }
//        }
//    }


//    @Composable
//    fun ShowFeed(postList: List<UserPost>) {
//        LazyColumn {
//            items(postList) { post ->
//                CompositionLocalProvider(
//                    LocalLayoutDirection provides LayoutDirection.Ltr
//                ) {
//                    Row {
//                        Column(
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Row (
//                                Modifier
//                                    .fillMaxWidth()
//                                    .padding(6.dp),
//                                verticalAlignment = Alignment.CenterVertically,
//                                    ){
//                                AsyncImage(
//                                    model = ImageRequest.Builder(LocalContext.current)
//                                        .data(post.imageUrl.image)
//                                        .build(),
//                                    contentDescription = "${post.userName}Username",
//                                    contentScale = ContentScale.Crop,
//                                    modifier = Modifier
//                                        .size(40.dp)
//                                        .clip(CircleShape)
//                                        .border(1.dp, Color.Black, CircleShape)
//                                )
//                                Text(
//                                    text = post.userName,
//                                    style = TextStyle(
//                                        color = Color.Black,
//                                        fontWeight = FontWeight.Bold,
//                                    ),
//                                    modifier = Modifier.padding(start = 6.dp)
//                                )
//                            }
//                            AsyncImage(
//                                model = ImageRequest.Builder(LocalContext.current)
//                                    .data(post.imageUrl.image)
//                                    .build(),
//                                contentDescription = post.caption,
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .aspectRatio(1f)
//                            )
//                            Text(
//                                buildAnnotatedString {
//                                    withStyle(
//                                        style = SpanStyle(
//                                            fontWeight = FontWeight.Bold
//                                        )
//                                    ) {
//                                        append(post.userName)
//                                    }
//                                    append(": ${post.caption}")
//                                },
//                                modifier = Modifier.padding(10.dp)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
}