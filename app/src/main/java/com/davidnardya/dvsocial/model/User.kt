package com.davidnardya.dvsocial.model

data class User (
    val userId: String,
    val userName: String,
    val posts: List<UserPost>
        )