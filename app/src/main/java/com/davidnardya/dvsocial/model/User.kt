package com.davidnardya.dvsocial.model

data class User (
    val userId: String,
    val userName: String,
    val password: String,
    val posts: List<UserPost>,
    val notifications: List<UserNotification>
        )