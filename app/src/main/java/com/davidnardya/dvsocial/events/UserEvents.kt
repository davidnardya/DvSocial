package com.davidnardya.dvsocial.events

sealed class UserEvents {
    class OnLogIn(val username: String, val password: String): UserEvents()
    class OnLogOut(): UserEvents()
    class OnNewUserCreated(val username: String, val password: String): UserEvents()
}
