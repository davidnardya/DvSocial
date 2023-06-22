package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.R
import com.davidnardya.dvsocial.viewmodel.ChatViewModel
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun ChatScreen(navController: NavHostController, chatViewModel: ChatViewModel) {
    ChatTheme {
        ChannelsScreen(
            title = stringResource(id = R.string.app_name),
            isShowingSearch = true,
            onItemClick = { channel ->
                chatViewModel.chatChannelIdLiveData.value = channel.id
                navController.navigate(route = Screen.ChatChannel.route)
            },
            onBackPressed = {
                navController.navigate(route = Screen.Feed.route) {
                    popUpTo(Screen.Feed.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
}