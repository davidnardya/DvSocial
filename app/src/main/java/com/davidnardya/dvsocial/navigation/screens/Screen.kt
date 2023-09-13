package com.davidnardya.dvsocial.navigation.screens

sealed class Screen(val route: String) {
    object Login: Screen("LoginScreen")
    object Registration: Screen("RegistrationScreen")
    object Feed: Screen("FeedScreen")
    object Splash: Screen("SplashScreen")
    object Chat: Screen("ChatScreen")
    object ChatChannel: Screen("ChatChannelScreen")
    object PostComments: Screen("PostComments")
}
