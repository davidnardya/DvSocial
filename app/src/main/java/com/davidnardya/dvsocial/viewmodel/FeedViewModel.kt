package com.davidnardya.dvsocial.viewmodel

import androidx.lifecycle.ViewModel
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.utils.Constants

class FeedViewModel: ViewModel() {

    fun getPosts() : List<UserPost> {
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
}