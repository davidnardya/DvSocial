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
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.davidnardya.dvsocial.navigation.SetupNavGraph
import com.davidnardya.dvsocial.navigation.screens.Screen
import com.davidnardya.dvsocial.viewmodel.ChatViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var feedViewModel: FeedViewModel

    @Inject
    lateinit var chatViewModel: ChatViewModel

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActivity()
        initObservers()
        initGetSteamChat()

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
                    chatViewModel = chatViewModel,
                    context = this
                )
            }
        }
    }

    private fun initGetSteamChat() {
        // 1 - Set up the OfflinePlugin for offline storage
        val offlinePluginFactory = StreamOfflinePluginFactory(
            config = Config(
                backgroundSyncEnabled = true,
                userPresence = true,
                persistenceEnabled = true,
                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
            ),
            appContext = applicationContext,
        )

        // 2 - Set up the client for API calls and with the plugin for offline storage
        val client = ChatClient.Builder("b67pax5b2wdq", applicationContext)
            .withPlugin(offlinePluginFactory)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()

        // 3 - Authenticate and connect the user
        val user = User(
            id = "tutorial-droid",
            name = "Tutorial Droid",
            image = "https://bit.ly/2TIt8NR"
        )
        client.connectUser(
            user = user,
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.NhEr0hP9W9nwqV7ZkdShxvi02C5PR7SJE7Cs4y7kyqg"
        ).enqueue()

        // Creating a channel
        val channelClient = client.channel(channelType = "messaging", channelId = "general")

        val extraData = mutableMapOf<String, Any>(
            "name" to "Awesome channel about food"
        )
        channelClient.create(memberIds = emptyList(), extraData = extraData).enqueue() { result ->
            if (result.isSuccess) {
                val channel: Channel = result.data()
            } else {
                Log.d("ChannelError", "Something went wrong creating a channel")
            }
        }

    }

    private fun initObservers() {
        feedViewModel.currentUser.observe(this) {
            if (
                it != null &&
                it.userName.isNotEmpty() && it.password.isNotEmpty() &&
                it.userName != "null" && it.password != "null"
            ) {
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