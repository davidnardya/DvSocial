package com.davidnardya.dvsocial.utils

import com.davidnardya.dvsocial.events.UserEvents
import com.davidnardya.dvsocial.repositories.UserRepository
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class UserAuthenticator @Inject constructor(private val userRepository: UserRepository) {

    suspend fun handleUserEvent(event: UserEvents) {
        when (event) {
            is UserEvents.OnLogIn -> authLogin(event.username, event.password)
            is UserEvents.OnNewUserCreated -> authRegistration(event.username, event.password)
            else -> {}
        }
    }

    private suspend fun authLogin(username: String, password: String) {
        var result = false
        userRepository.getUserListFlow().value.forEach { user ->
            if (
                user.username != "" && user.password != "" &&
                user.username == username && user.password == password &&
                !userRepository.getUserLoggedIn()
            ) {
                result = true
                userLoginAuthProduceResult.send(true)
            }
        }
        if (result) {
            userRepository.saveUserInfo(username, password)
        } else {
            userLoginAuthProduceResult.send(false)
        }

    }

    private suspend fun authRegistration(username: String, password: String) {
        var result = true
        userRepository.getUserListFlow().value.forEach { user ->
            if (user.username == username) {
                showToast("ERROR: Username already taken, try another")
                result = false
            }
        }

        if (result) {
            userRepository.saveUserInfo(username, password, true)
        }
    }
}

val userLoginAuthProduceResult = Channel<Boolean>()