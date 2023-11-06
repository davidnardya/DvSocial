package com.davidnardya.dvsocial.navigation.screens

sealed class Screen(val route: String) {
    object Login: Screen("LoginScreen")
    object Registration: Screen("RegistrationScreen")
    object Feed: Screen("FeedScreen")
    object Splash: Screen("SplashScreen")
    object PostComments: Screen("PostCommentsScreen")
    object UserProfile: Screen("UserProfileScreen")
    object Notifications: Screen("NotificationsScreen")
    object Post: Screen("PostScreen")
    object Comment: Screen("CommentScreen")
    object Camera: Screen("CameraScreen")
}
