package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.davidnardya.dvsocial.R
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun ChatScreen() {
    ChatTheme {
        ChannelsScreen(
            title = stringResource(id = R.string.app_name),
            isShowingSearch = true,
            onItemClick = { channel ->
                // TODO Start Messages Activity
            },
            onBackPressed = { /*finish()*/ }
        )
    }
}