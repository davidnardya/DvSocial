package com.davidnardya.dvsocial.model

import com.google.gson.annotations.SerializedName

data class UserComment(
    val text: String?,
    @SerializedName("is-liked")
    override var isLiked: Boolean?,
    override var likes: Int?,
    val username: String?
) : Likeable
