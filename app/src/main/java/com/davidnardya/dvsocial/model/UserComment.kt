package com.davidnardya.dvsocial.model

data class UserComment(
    override val id: String?,
    val text: String?,
    override var isLiked: Boolean?,
    override var likes: Int?,
) : Likeable
