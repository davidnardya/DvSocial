package com.davidnardya.dvsocial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val isUserLoggedInLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    private val currentUserLiveData: MutableLiveData<User> = MutableLiveData()
    private val failedLogins: MutableLiveData<Int> = MutableLiveData(0)

    private fun getUsersFlow(): Flow<List<User>> = userRepository.getUserListFlow()

    fun getFeedPostList() : List<UserPost> {
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

    suspend fun userAttemptLogin(userName: String, password: String): Boolean {
//        userLoggedInLiveData.value?.let {
//            return if (userName == Constants.loggedInUser.userName && password == Constants.loggedInUser.password) {
//                currentUserLiveData.value = Constants.loggedInUser
//                userLoggedInLiveData.value = true
//                it
//            } else {
//                userLoggedInLiveData.value = false
//                failedLogins.value = failedLogins.value?.plus(1)
//                it
//            }
//        } ?: kotlin.run {
//            return false
//        }
        return userRepository.getUserInfo().first == userName &&
                userRepository.getUserInfo().second == password

    }

    fun registerUser(userName: String, password: String) {
        viewModelScope.launch {
            userRepository.saveUserInfo(userName,password)
        }
    }

    fun userLogOut() {
        viewModelScope.launch {
            userRepository.saveUserInfo("","")
        }
    }

    fun getFailedLogins(): MutableLiveData<Int> = failedLogins
    fun getIsUserLoggedIn(): MutableLiveData<Boolean> = isUserLoggedInLiveData

}