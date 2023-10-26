package com.davidnardya.dvsocial.utils

import android.util.Log
import com.davidnardya.dvsocial.events.UserEvents
import com.davidnardya.dvsocial.repositories.UserRepository
import javax.inject.Inject

class UserAuthenticator @Inject constructor(private val userRepository: UserRepository) {

    suspend fun handleUserEvent(event: UserEvents) {

        when(event) {
            is UserEvents.OnLogIn -> authLogin(event.username,event.password)
            is UserEvents.OnNewUserCreated -> authRegistration(event.username,event.password)
            else -> {}
        }
    }

    private suspend fun authLogin(username: String, password: String) {
        Log.d("123321","authLogin init, username $username")
    }

    private suspend fun authRegistration(username: String, password: String) {
        Log.d("123321","authRegistration init, username $username")
        var result = true
        userRepository.getUserListFlow().collect { list ->
            list.forEach { user ->
                if (user.username == username) {
                    showToast("ERROR: Username already taken, try another")
                    result = false
                    return@collect
                }
            }
            if(result) {
                userRepository.saveUserInfo(username, password)
            }

        }

    }
}