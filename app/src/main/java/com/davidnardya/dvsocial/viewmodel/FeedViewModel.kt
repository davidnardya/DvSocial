package com.davidnardya.dvsocial.viewmodel

import android.util.Log
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
                user.posts?.forEach {
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
            userRepository.subscribeToCurrentUserFlow()
            while (i) {
                delay(1000L)
                if (getFeedPostList().isNotEmpty() && userRepository.getIsUserLoggedIn()) {
                    i = false
                    isLoadingComplete.value = true
                }
            }
        }
    }

    fun uploadNewUserPost(newPost: UserPost, id: String?, user: DvUser?) {
        viewModelScope.launch {
            Log.d("123321","uploadNewUserPost viewmodel")
            userRepository.uploadNewUserPost(newPost, id, user)
        }
    }

}