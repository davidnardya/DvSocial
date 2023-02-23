package com.davidnardya.dvsocial.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val currentUser: MutableLiveData<User> = MutableLiveData()
    val isLoadingComplete: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun getUsersFlow(): MutableStateFlow<MutableList<User>> =
        userRepository.getUserListFlow()

    private fun getCurrentUserFlow(): Flow<User> = userRepository.getCurrentUserFlow()

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

    fun getCurrentUser(): User? {
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
            if (user.userName != "" && user.password != "" && user.userName == userName && user.password == password) {
                result = true
            }
        }
        return result
    }

    fun registerUser(userName: String, password: String) {
        viewModelScope.launch {
            userRepository.saveUserInfo(userName, password)
        }
    }

    fun userLogOut() {
        viewModelScope.launch {
            userRepository.saveUserInfo("", "")
        }
    }

    fun checkFeedPostList() {
        viewModelScope.launch {
            var i = true
            while (i) {
                delay(1000L)
                Log.d("123321", "Delay")
                if (getFeedPostList().isNotEmpty()) {
                    i = false
                    isLoadingComplete.value = true
                    Log.d("123321", "Stop")
                }
            }
        }
    }
}