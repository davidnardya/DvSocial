package com.davidnardya.dvsocial.model

data class UserPost (
    override val id: String? = "",
    var userName: String? = "",
    val imageUrl: UserImage? = null,
    val caption: String? = "",
    val comments: List<UserComment>? = null,
    override var isLiked: Boolean? = false,
    override var likes: Int? = 0,
) : Likeable