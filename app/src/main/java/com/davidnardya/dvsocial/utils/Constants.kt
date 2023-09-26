package com.davidnardya.dvsocial.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserComment
import com.davidnardya.dvsocial.model.UserNotification
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
        emptyList(),
        emptyList()
    )

    const val BASE_URL = "https://dog.ceo/api/"
    const val USER_NAME = "username"
    const val PASSWORD = "password"
    const val DID_LOG_IN = "didLogIn"

    const val USER_PROFILE_ID = "user_profile"
    const val USER_PROFILE_TITLE = "My Profile"
    const val NOTIFICATIONS_ID = "notifications"
    const val NOTIFICATIONS_TITLE = "Notifications"
    const val FEED_ID = "feed"
    const val FEED_TITLE = "Feed"
    const val CHAT_ID = "chat"
    const val CHAT_TITLE = "Chat"
    const val SETTINGS_ID = "settings"
    const val SETTINGS_TITLE = "Settings"
    const val LOGOUT_ID = "logout"
    const val LOGOUT_TITLE = "Log Out"

    val menuList = listOf(
        MenuItem(
            USER_PROFILE_ID,
            USER_PROFILE_TITLE,
            Icons.Default.Person
        ),
        MenuItem(
            NOTIFICATIONS_ID,
            NOTIFICATIONS_TITLE,
            Icons.Default.Notifications
        ),
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

    private val commentsListOne = listOf(
        UserComment(
            "1111",
            "this is a text for a short comment",
            true,
            0
        ),
        UserComment(
            "1112",
            "this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment ",
            false,
            4
        ),
        UserComment(
            "1113",
            "this is a text for a short comment",
            false,
            1
        ),
        UserComment(
            "1114",
            "this is a text for a short comment",
            false,
            10
        ),
        UserComment(
            "1115",
            "this is a text for a short comment",
            true,
            0
        ),
        UserComment(
            "1116",
            "this is a text for a short comment",
            false,
            0
        ),
    )

    private val commentsListTwo = listOf(
        UserComment(
            "1111",
            "this is a text for a short comment",
            false,
            0
        )
    )

    private val commentsListThree = listOf(
        UserComment(
            "1115",
            "this is a text for a short comment",
            false,
            9
        ),
        UserComment(
            "1112",
            "this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment ",
            false,
            1
        ),
        UserComment(
            "1113",
            "this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment ",
            true,
            5
        ),
        UserComment(
            "1114",
            "this is a text for a short comment",
            false,
            0
        )
    )

    val mockComments = listOf(
        commentsListOne, commentsListTwo, commentsListThree, null
    )

    val mockNotifications = listOf(
        UserNotification(
            "1",
            "Someone liked your post!"
        ),
        UserNotification(
            "2",
            "Someone liked your comment!"
        ),
        UserNotification(
            "3",
            "Someone shared your post!"
        ),
        UserNotification(
            "4",
            "Someone liked your comment!"
        ),
        UserNotification(
            "5",
            "Someone liked your post!"
        )
    )
}