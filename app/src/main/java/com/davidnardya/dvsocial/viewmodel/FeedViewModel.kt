package com.davidnardya.dvsocial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val currentUser: MutableLiveData<DvUser> = MutableLiveData()
    val isLoadingComplete: MutableLiveData<Boolean> = MutableLiveData(false)
    val currentPost: MutableLiveData<UserPost> = MutableLiveData()

    private fun getUsersFlow(): MutableStateFlow<MutableList<DvUser>> =
        userRepository.getUserListFlow()

    private fun getCurrentUserFlow(): Flow<DvUser> = userRepository.getCurrentUserFlow()

    fun getFeedPostList(): MutableList<UserPost> {
        val postList = mutableListOf<UserPost>()
        getUsersFlow().onEach { userList ->
            userList.forEach { user ->
                user.posts.forEach {
                    postList.add(it)
                }
            }
        }.launchIn(viewModelScope)
        return postList
    }

    fun getCurrentUser(): DvUser? {
        getCurrentUserFlow().map {
            if (
                it.userName.isNotEmpty() && it.password.isNotEmpty() &&
                it.userName != "null" && it.password != "null"
            ) {
                currentUser.value = it
            }
        }.launchIn(viewModelScope)
        return currentUser.value
    }

    fun subscribeToUserListFlow() {
        viewModelScope.launch {
            userRepository.subscribeToUserListFlow()
        }
    }

    fun subscribeToCurrentUserFlow() {
        viewModelScope.launch {
            userRepository.subscribeToCurrentUserFlow()
        }
    }

    fun userAttemptLogin(userName: String, password: String): Boolean {
        var result = false
        viewModelScope.launch {
            val user = userRepository.getUserInfo()
            if (
                user.userName != "" && user.password != "" &&
                user.userName == userName && user.password == password &&
                !userRepository.getUserLoggedIn()
            ) {
                result = true
                userRepository.saveUserLoggedIn(true)
                currentUser.value = user
            }
        }
        return result
    }

    suspend fun getUserLoggedIn() = userRepository.getUserLoggedIn()


    fun registerUser(userName: String, password: String) {
        viewModelScope.launch {
            userRepository.saveUserInfo(userName, password)
        }
    }

    fun userLogOut() {
        viewModelScope.launch {
            userRepository.saveUserInfo("", "")
            userRepository.saveUserLoggedIn(false)
            userRepository.clearDataStore()
        }
    }

    fun checkFeedPostList() {
        viewModelScope.launch {
            var i = true
            while (i) {
                delay(1000L)
                if (getFeedPostList().isNotEmpty() && currentUser.value != null) {
                    i = false
                    isLoadingComplete.value = true
                }
            }
        }
    }
}