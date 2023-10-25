package com.davidnardya.dvsocial.model

data class UserComment(
    val text: String?,
    override var isLiked: Boolean?,
    override var likes: Int?,
) : Likeable
