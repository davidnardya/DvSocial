package com.davidnardya.dvsocial.utils

import android.util.Log
import com.davidnardya.dvsocial.events.UserEvents
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.repositories.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
        var loggedUser: DvUser

        val usernameFromPDS = userRepository.getStringFromPreferenceDataStore(Constants.USER_NAME)
        val passwordFromPDS = userRepository.getStringFromPreferenceDataStore(Constants.PASSWORD)

        delay(2000)
        userRepository.getUserListFlow().value.map { user ->
            Log.d("123321","authLogin username ${user.username}")
            Log.d("123321","authLogin usernameFromPDS $usernameFromPDS")
            Log.d("123321","authLogin authLogin(username: $username")
            Log.d("123321","authLogin userRepository.getIsUserLoggedIn(): ${userRepository.getIsUserLoggedIn()}")
            if (
                user.username != "" && user.password != "" &&
                user.username == username && user.password == password/* &&
                !userRepository.getIsUserLoggedIn() */||
                user.username != "" && user.password != "" &&
                user.username == usernameFromPDS && user.password == passwordFromPDS /*&&
                !userRepository.getIsUserLoggedIn()*/

            ) {
                Log.d("123321","authLogin after ${user.username}")
                result = true
                loggedUser = user
                userRepository.saveLoggedInUser(loggedUser)
                userLoginAuthProduceResult.send(true)
            }
        }
        if (!result) {
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
            userRepository.registerNewUser(username, password)
        }
    }
}

val userLoginAuthProduceResult = Channel<Boolean>()