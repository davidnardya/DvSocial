package com.davidnardya.dvsocial.model

data class UserComment(
    val id: Int,
    val text: String,
    var isLiked: Boolean = false
)
