package com.davidnardya.dvsocial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.utils.Constants

class FeedViewModel : ViewModel() {

    private val userLoggedInLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    private val currentUserLiveData: MutableLiveData<User> = MutableLiveData()

    fun getPosts(): List<UserPost> {
        val users = listOf(
            Constants.userI,
            Constants.userII,
            Constants.userIII
        )
        val posts = mutableListOf<UserPost>()
        users.forEach { user ->
            user.posts.forEach { post ->
                post.userName = user.userName
                posts.add(post)
            }
        }
        return posts
    }

    fun userAttemptLogin(userName: String, password: String) : Boolean {
        userLoggedInLiveData.value?.let {
            return if (userName == Constants.loggedInUser.userName && password == Constants.loggedInUser.password) {
                currentUserLiveData.value = Constants.loggedInUser
                userLoggedInLiveData.value = true
                it
            } else {
                userLoggedInLiveData.value = false
                it
            }
        } ?: kotlin.run {
            return false
        }

    }
}