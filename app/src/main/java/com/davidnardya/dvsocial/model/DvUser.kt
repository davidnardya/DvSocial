package com.davidnardya.dvsocial.model

data class DvUser (
    val username: String? = "",
    val password: String? = "",
    val posts: List<UserPost>? = emptyList(),
    val notifications: List<UserNotification>? = emptyList()
        )