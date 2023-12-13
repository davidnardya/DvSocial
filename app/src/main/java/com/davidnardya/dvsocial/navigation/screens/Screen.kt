package com.davidnardya.dvsocial.navigation.screens

import android.net.Uri

sealed class Screen(val route: String, var uri:Uri? = null) {
    object Login: Screen("LoginScreen")
    object Registration: Screen("RegistrationScreen")
    object Feed: Screen("FeedScreen")
    object Splash: Screen("SplashScreen")
    object PostComments: Screen("PostCommentsScreen")
    object UserProfile: Screen("UserProfileScreen")
    object Notifications: Screen("NotificationsScreen")
    object Post: Screen("PostScreen")
    object Comment: Screen("CommentScreen")
    object PhotoPick: Screen("CameraScreen")
    object PostFromNotification: Screen("PostFromNotificationScreen")
}
