package com.davidnardya.dvsocial.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.navigation.navdrawer.MenuItem


object Constants {

    val userNameList = listOf(
        "MyNameIsPaul",
        "ThisIsAUserName",
        "FeckinHeckin",
        "BabyYoda",
        "Shakalaka"
    )

    val userImageCaptionList = listOf(
        "Look at my doggo! His name is Goddo",
        "Look at my doggo! His name is Rex",
        "Look at my doggo! His name is Ben",
        "Look at my doggo! His name is Loopy",
        "Look at my doggo! His name is Tie",
        "Look at my doggo! His name is Do-go-go"
    )

    val emptyUser: User = User(
        "",
        "",
        "",
        emptyList()
    )

    const val BASE_URL = "https://dog.ceo/api/"
    const val USER_NAME = "username"
    const val PASSWORD = "password"

    const val FEED_ID = "feed"
    const val FEED_TITLE = "feed"
    const val CHAT_ID = "chat"
    const val CHAT_TITLE = "Chat"
    const val SETTINGS_ID = "settings"
    const val SETTINGS_TITLE = "Settings"
    const val LOGOUT_ID = "logout"
    const val LOGOUT_TITLE = "Log Out"

    val menuList = listOf(
        MenuItem(
            FEED_ID,
            FEED_TITLE,
            Icons.Default.Home
        ),
        MenuItem(
            CHAT_ID,
            CHAT_TITLE,
            Icons.Default.Email
        ),
        MenuItem(
            SETTINGS_ID,
            SETTINGS_TITLE,
            Icons.Default.Settings
        ),
        MenuItem(
            LOGOUT_ID,
            LOGOUT_TITLE,
            Icons.Default.ExitToApp
        )
    )
}