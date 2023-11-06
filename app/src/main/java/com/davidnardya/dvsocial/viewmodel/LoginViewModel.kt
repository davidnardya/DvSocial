package com.davidnardya.dvsocial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.dvsocial.events.UserEvents
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.repositories.UserRepository
import com.davidnardya.dvsocial.utils.produceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun getEventsFlow(): Flow<UserEvents> = eventsFlow
    private val eventsFlow = MutableSharedFlow<UserEvents>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )

    val currentUser: MutableLiveData<DvUser> = MutableLiveData()
    private fun getCurrentUserFlow(): Flow<DvUser> = userRepository.getCurrentUserFlow()

    fun getCurrentUser(): DvUser? {
        getCurrentUserFlow().map {
            if (
                it.username?.isNotEmpty() == true && it.password?.isNotEmpty() == true &&
                it.username != "null" && it.password != "null"
            ) {
                currentUser.value = it
            }
        }.launchIn(viewModelScope)
        return currentUser.value
    }

    fun registerUser(userName: String, password: String) {
//        viewModelScope.launch {
//            userRepository.saveUserInfo(userName, password)
//        }
        eventsFlow.tryEmit(UserEvents.OnNewUserCreated(userName, password))
    }

    fun userLogOut() {
        viewModelScope.launch {
            userRepository.saveUserInfo("", "")
            userRepository.saveUserLoggedIn(false)
            userRepository.clearDataStore()
        }
    }

    suspend fun userAttemptLogin(username: String, password: String): Boolean {
        var result = false
        eventsFlow.tryEmit(UserEvents.OnLogIn(username, password))
        viewModelScope.launch {
            val user = userRepository.getUserInfo()
            if (produceResult.receive()) {
                result = true
                userRepository.saveUserLoggedIn(true)
                currentUser.value = user
            } else {
                if (
                    user.username != "" && user.password != "" &&
                    user.username == username && user.password == password &&
                    !userRepository.getUserLoggedIn()
                ) {
                    result = true
                    userRepository.saveUserLoggedIn(true)
                    currentUser.value = user
                }
            }
        }
        return result
    }

    suspend fun getUserLoggedIn() = userRepository.getUserLoggedIn()

    fun subscribeToCurrentUserFlow() {
        viewModelScope.launch {
            userRepository.subscribeToCurrentUserFlow()
        }
    }

    fun subscribeToUserEventsFlow() {
        viewModelScope.launch {

        }
    }
}