package com.davidnardya.dvsocial.model

data class User (
    val userId: String,
    val password: String,
    val userName: String,
    val posts: List<UserPost>
        )