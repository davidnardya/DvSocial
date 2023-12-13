package com.davidnardya.dvsocial.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.model.UserComment
import com.davidnardya.dvsocial.model.UserNotification
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
    val postList: MutableLiveData<MutableList<UserPost>> = MutableLiveData()

    private fun getUsersFlow(): MutableStateFlow<MutableList<DvUser>> =
        userRepository.getUserListFlow()


    fun getFeedPostList() {
        val newPostList = mutableListOf<UserPost>()
        getUsersFlow().onEach { userList ->
            userList.forEach { user ->
                user.posts?.forEach {
                    it.username = user.username
                    newPostList.add(it)
                }
            }
        }.launchIn(viewModelScope)
        postList.value = newPostList
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
                if (
                    postList.value?.isNotEmpty() == true &&
                    userRepository.getIsUserLoggedIn() &&
                    Constants.currentUser != null
                ) {
                    i = false
                    isLoadingComplete.value = true
                }
            }
        }
    }

    fun generateNewId() = userRepository.generateNewId()

    private fun addNotification(
        newNotification: UserNotification
    ) = userRepository.addNotification(newNotification)

    fun updateCommentLikes(
        commentId: String?,
        postId: String?,
        userId: String?
    ) {
        userRepository.updateCommentLikes(commentId, postId, userId)
        addNotification(
            UserNotification(
                "${Constants.currentUser?.username} liked your comment",
                userId,
                postId,
                commentId
            )
        )
    }

    fun updatePostLikes(postId: String?, userId: String?) {
        userRepository.updatePostLikes(postId, userId)
        addNotification(
            UserNotification(
                "${Constants.currentUser?.username} liked your comment",
                userId,
                postId
            )
        )
    }

    fun uploadNewUserComment(
        newComment: UserComment
    ) {
        userRepository.uploadNewUserComment(newComment)
        addNotification(
            UserNotification(
                "${Constants.currentUser?.username} commented on your post",
                newComment.userId,
                newComment.postId,
                newComment.id
            )
        )
    }

    fun uploadNewUserPost(newPost: UserPost) =
        userRepository.uploadNewUserPost(newPost)

}