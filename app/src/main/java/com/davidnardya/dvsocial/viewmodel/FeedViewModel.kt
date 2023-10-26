package com.davidnardya.dvsocial.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.repositories.UserRepository
import com.davidnardya.dvsocial.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {


    val isLoadingComplete: MutableLiveData<Boolean> = MutableLiveData(false)
    val currentPostState = mutableStateOf(UserPost(comments = Constants.commentsListOne))

    private fun getUsersFlow(): MutableStateFlow<MutableList<DvUser>> =
        userRepository.getUserListFlow()



    fun getFeedPostList(): MutableList<UserPost> {
        val postList = mutableListOf<UserPost>()
        getUsersFlow().onEach { userList ->
            userList.forEach { user ->
                user.posts.forEach {
                    it.username = user.username
                    postList.add(it)
                }
            }
        }.launchIn(viewModelScope)
        return postList
    }



    fun subscribeToUserListFlow() {
        viewModelScope.launch {
            userRepository.subscribeToUserListFlow()
        }
    }

    fun checkFeedPostList() {
        viewModelScope.launch {
            var i = true
            while (i) {
                delay(1000L)
                if (getFeedPostList().isNotEmpty() && userRepository.getUserLoggedIn()) {
                    i = false
                    isLoadingComplete.value = true
                }
            }
        }
    }
}