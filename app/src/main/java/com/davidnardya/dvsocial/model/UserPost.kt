package com.davidnardya.dvsocial.model

data class UserPost (
    val postId: String,
    var userName: String,
    val imageUrl: String,
    val caption: String
        )