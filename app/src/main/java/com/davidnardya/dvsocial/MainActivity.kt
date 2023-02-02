package com.davidnardya.dvsocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.lifecycleScope
import com.davidnardya.dvsocial.model.ScreenOptions
import com.davidnardya.dvsocial.model.UserPost
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: FeedViewModel

    private val stateLogin = mutableStateOf(ScreenOptions.SHOW_LOGIN)
    private val loginFailures = mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getFailedLogins().observe(this) {
            loginFailures.value = it
        }
        viewModel.getIsUserLoggedIn().observe(this) {
            if(it) {
                stateLogin.value = ScreenOptions.SHOW_FEED
            }
        }
        viewModel.subscribeToUserListFlow()

        setContent {
            when(stateLogin.value) {
                ScreenOptions.SHOW_LOGIN -> {
                    ShowLogin()
                }
                ScreenOptions.SHOW_FEED -> {
                    ShowFeed(viewModel.getFeedPostList())
                }
                ScreenOptions.SHOW_REGISTRATION -> {
                    ShowRegistration()
                }
            }
        }
    }

    @Composable
    fun ShowRegistration() {
        var userName by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = userName,
                    label = { Text(text = "Username")},
                    onValueChange = { userName = it},
                    placeholder = { Text(text = "Enter your username")}
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = password,
                    label = { Text(text = "Password")},
                    onValueChange = { password = it},
                    placeholder = { Text(text = "Enter your password")}
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { handleRegistrationClick(userName, password) }
                ) {
                    Text(text = "Register!")
                }
            }
        }
    }

    private fun handleRegistrationClick(userName: String, password: String) {
        viewModel.registerUser(userName, password)
    }

    @Composable
    fun ShowLogin() {
        var userName by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = userName,
                    label = { Text(text = "Username") },
                    onValueChange = { userName = it },
                    placeholder = { Text(text = "Enter your username") },
                    textStyle = handleLoginFailure(loginFailures.value)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = password,
                    label = { Text(text = "Password") },
                    onValueChange = { password = it },
                    placeholder = { Text(text = "Enter your password") },
                    textStyle = handleLoginFailure(loginFailures.value)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { handleLoginClick(userName, password) }
                ) {
                   Text(text = "Log in!")
                }
            }

        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { stateLogin.value = ScreenOptions.SHOW_REGISTRATION }
                ) {
                    Text(text = "Don't have a user yet? Register now!")
                }
            }
        }

    }

    @Composable
    fun handleLoginFailure(loginFailures: Int) : TextStyle {
        return if(loginFailures in 1..3) {
            TextStyle(color = Color.Red)
        } else if(loginFailures > 3) {
            TextStyle(color = Color.Gray)
        } else {
            TextStyle(color = Color.Unspecified)
        }
    }

    private fun handleLoginClick(userName: String, password: String) {
        lifecycleScope.launch {
            if(viewModel.userAttemptLogin(userName,password)) {
                stateLogin.value = ScreenOptions.SHOW_FEED
            }
        }
    }

    @Composable
    fun ShowFeed(postList: List<UserPost>) {
        LazyColumn {
            items(postList) { post ->
                CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Ltr
                ) {
                    Row {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row (
                                Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                    ){
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(post.imageUrl.image)
                                        .build(),
                                    contentDescription = "${post.userName}Username",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, Color.Black, CircleShape)
                                )
                                Text(
                                    text = post.userName,
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    modifier = Modifier.padding(start = 6.dp)
                                )
                            }
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(post.imageUrl.image)
                                    .build(),
                                contentDescription = post.caption,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(post.userName)
                                    }
                                    append(": ${post.caption}")
                                },
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}