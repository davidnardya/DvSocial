package com.davidnardya.dvsocial.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.dvsocial.events.UserEvents
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.repositories.UserRepository
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.utils.Constants.PASSWORD
import com.davidnardya.dvsocial.utils.Constants.USER_NAME
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

    val currentUser: MutableLiveData<DvUser?> = MutableLiveData(Constants.currentUser)

    suspend fun setCurrentUser() {
        val username = userRepository.getStringFromPreferenceDataStore(USER_NAME)
        val password = userRepository.getStringFromPreferenceDataStore(PASSWORD)
        eventsFlow.tryEmit(UserEvents.OnLogIn(username,password))
    }


    fun registerUser(userName: String, password: String) {
        eventsFlow.tryEmit(UserEvents.OnNewUserCreated(userName, password))
    }

    fun logUserOut() {
        viewModelScope.launch {
            userRepository.logUserOut()
        }
    }

    fun userAttemptLogin(username: String, password: String) {
//        var result: Boolean = false
        eventsFlow.tryEmit(UserEvents.OnLogIn(username, password))
//        result = userLoginAuthProduceResult.receive()
//        return result
    }

    suspend fun getUserLoggedIn() = userRepository.getIsUserLoggedIn()

    fun subscribeToCurrentUserFlow() {
        viewModelScope.launch {
            userRepository.eventsFlow = eventsFlow
            userRepository.subscribeToCurrentUserFlow()
        }
    }

    fun uploadImage(uri: Uri): String = userRepository.uploadImage(uri)
    fun getImageDownloadUrl(path: String) = userRepository.getImageDownloadUrl(path, viewModelScope)
}