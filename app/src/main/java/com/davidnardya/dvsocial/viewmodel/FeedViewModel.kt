package com.davidnardya.dvsocial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val failedLogins: MutableLiveData<Int> = MutableLiveData(0)
    val currentUser: MutableLiveData<User> = MutableLiveData()

    private fun getUsersFlow(): Flow<List<User>> = userRepository.getUserListFlow()

    fun getFeedPostList(): List<UserPost> {
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

    fun subscribeToUserListFlow() {
        viewModelScope.launch {
            userRepository.loadUsersToFeed()
        }
    }

    fun userAttemptLogin(userName: String, password: String): Boolean {
        var result = false
        currentUser.value?.let {
            if (it.userName == userName && it.password == password) {
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

    fun getFailedLogins(): MutableLiveData<Int> = failedLogins

    suspend fun getCurrentUser(): User {
        var user: User
        withContext(Dispatchers.Default) { user = userRepository.getUserInfo() }
        return user
    }
}