package com.davidnardya.dvsocial.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.model.UserComment
import com.davidnardya.dvsocial.model.UserNotification
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.navigation.navdrawer.MenuItem


object Constants {

    var currentUser: DvUser? = null

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

    val emptyUser: DvUser = DvUser(
        "",
        "",
        "",
        emptyList(),
        emptyList()
    )

    const val BASE_URL = "https://dvsocial-9eff2-default-rtdb.europe-west1.firebasedatabase.app/"
    const val USER_NAME = "username"
    const val PASSWORD = "password"
    const val USER_ID = "userid"
    const val DID_LOG_IN = "didLogIn"

    const val USER_PROFILE_ID = "user_profile"
    const val USER_PROFILE_TITLE = "My Profile"
    const val NOTIFICATIONS_ID = "notifications"
    const val NOTIFICATIONS_TITLE = "Notifications"
    const val FEED_ID = "feed"
    const val FEED_TITLE = "Feed"
    const val SETTINGS_ID = "settings"
    const val SETTINGS_TITLE = "Settings"
    const val LOGOUT_ID = "logout"
    const val LOGOUT_TITLE = "Log Out"
    const val MAX_COMMENT_LENGTH = 200

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

    val commentsListOne = listOf(
        UserComment(
            "this is a text for a short comment",
            true,
            0,
            ""
        ),
        UserComment(
            "this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment ",
            false,
            4,
            ""
        ),
        UserComment(
            "this is a text for a short comment",
            false,
            1,
            ""
        ),
        UserComment(
            "this is a text for a short comment",
            false,
            10,
            ""
        ),
        UserComment(
            "this is a text for a short comment",
            true,
            0,
            ""
        ),
        UserComment(
            "this is a text for a short comment",
            false,
            0,
            ""
        ),
    )

    private val commentsListTwo = listOf(
        UserComment(
            "this is a text for a short comment",
            false,
            0
        )
    )

    private val commentsListThree = listOf(
        UserComment(
            "this is a text for a short comment",
            false,
            9
        ),
        UserComment(
            "this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment ",
            false,
            1
        ),
        UserComment(
            "this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment this is a text for a long comment ",
            true,
            5
        ),
        UserComment(
            "this is a text for a short comment",
            false,
            0
        )
    )

//    val mockComments = listOf(
//        commentsListOne, commentsListTwo, commentsListThree, null
//    )

    val mockNotifications = listOf(
        UserNotification(
            "Someone liked your post!"
        ),
        UserNotification(
            "Someone liked your comment!"
        ),
        UserNotification(
            "Someone shared your post!"
        ),
        UserNotification(
            "Someone liked your comment!"
        ),
        UserNotification(
            "Someone liked your post!"
        )
    )

    val mockPosts = listOf(
        UserPost(
            "https://images.dog.ceo/breeds/mountain-bernese/n02107683_4515.jpg",
            "This is a test caption20",
            commentsListOne,
            false,
            2,
            ""
        ),
        UserPost(
            "https://images.dog.ceo/breeds/stbernard/n02109525_1100.jpg",
            "This is a test caption10",
            commentsListTwo,
            false,
            0,
            ""
        ),
        UserPost(
            "https://images.dog.ceo/breeds/basenji/n02110806_6424.jpg",
            "This is a test caption10",
            commentsListThree,
            false,
            0,
            ""
        ),
        UserPost(
            "https://images.dog.ceo/breeds/eskimo/n02109961_7861.jpg",
            "This is a test caption10",
            commentsListOne,
            false,
            0,
            ""
        ),
        UserPost(
            "https://images.dog.ceo/breeds/terrier-norwich/n02094258_2801.jpg",
            "This is a test caption10",
            commentsListOne,
            false,
            0,
            ""
        ),
        UserPost(
            "https://images.dog.ceo/breeds/doberman/n02107142_5663.jpg",
            "This is a test caption10",
            commentsListOne,
            false,
            0,
            ""
        ),
        UserPost(
            "https://images.dog.ceo/breeds/terrier-irish/n02093991_1397.jpg",
            "This is a test caption10",
            commentsListOne,
            false,
            0,
            ""
        )
    )
}