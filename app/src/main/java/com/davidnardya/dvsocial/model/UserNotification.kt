package com.davidnardya.dvsocial.model

data class UserNotification (
    val text: String? = "",
    val userId: String? = "",
    val postId: String? = "",
    val commentId: String? = "",
    val isRead: Boolean? = false
)