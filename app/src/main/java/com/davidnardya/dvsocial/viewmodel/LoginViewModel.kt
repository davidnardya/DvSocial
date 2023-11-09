package com.davidnardya.dvsocial.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.dvsocial.events.UserEvents
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.repositories.UserRepository
import com.davidnardya.dvsocial.repositories.currentUserProduceResult
import com.davidnardya.dvsocial.utils.userLoginAuthProduceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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

    suspend fun setCurrentUser() {
        currentUser.value = currentUserProduceResult.receive()
        Log.d("123321","currentUser.value.username ${currentUser.value?.username}")
    }
//    private fun getCurrentUserFlow(): Flow<DvUser> = userRepository.getCurrentUserFlow()

//    fun getCurrentUser(): DvUser? {
//        getCurrentUserFlow().map {
//            if (
//                it.username?.isNotEmpty() == true && it.password?.isNotEmpty() == true &&
//                it.username != "null" && it.password != "null"
//            ) {
//                currentUser.value = it
//            }
//        }.launchIn(viewModelScope)
//        return currentUser.value
//    }

    fun registerUser(userName: String, password: String) {
        eventsFlow.tryEmit(UserEvents.OnNewUserCreated(userName, password))
    }

    fun logUserOut() {
        viewModelScope.launch {
            userRepository.logUserOut()
        }
    }

    suspend fun userAttemptLogin(username: String, password: String): Boolean {
        var result = false
        eventsFlow.tryEmit(UserEvents.OnLogIn(username, password))
//        viewModelScope.launch {
//            val user = userRepository.getUserInfo()
//            if (userLoginAuthProduceResult.receive()) {
//                result = true
//                userRepository.saveUserLoggedIn(true)
//                currentUser.value = user
//            } else {
//                if (
//                    user.username != "" && user.password != "" &&
//                    user.username == username && user.password == password &&
//                    !userRepository.getUserLoggedIn()
//                ) {
//                    result = true
//                    userRepository.saveUserLoggedIn(true)
//                    currentUser.value = user
//                }
//            }
//        }
        result = userLoginAuthProduceResult.receive()
        return result
    }

    suspend fun getUserLoggedIn() = userRepository.getIsUserLoggedIn()

    fun subscribeToCurrentUserFlow() {
        viewModelScope.launch {
            userRepository.eventsFlow = eventsFlow
            userRepository.subscribeToCurrentUserFlow()
        }
    }

    fun subscribeToUserEventsFlow() {
        viewModelScope.launch {

        }
    }

    fun uploadImage(uri: Uri): String = userRepository.uploadImage(uri)
    fun getImageDownloadUrl(path: String) = userRepository.getImageDownloadUrl(path, viewModelScope)
}