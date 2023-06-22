package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.runtime.Composable
import com.davidnardya.dvsocial.viewmodel.ChatViewModel
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun ChatChannelScreen(chatViewModel: ChatViewModel) {
    ChatTheme {
        MessagesScreen(
            channelId = chatViewModel.chatChannelIdLiveData.value ?: "",
            messageLimit = 30,
            onBackPressed = { }
        )
    }
}