package com.davidnardya.dvsocial.model

data class UserPost (
    val userName: String? = "",
    val imageUrl: UserImage? = null,
    val caption: String? = "",
    val comments: List<UserComment>? = null,
    override var isLiked: Boolean? = false,
    override var likes: Int? = 0,
) : Likeable