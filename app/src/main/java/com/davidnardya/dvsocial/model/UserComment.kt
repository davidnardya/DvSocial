package com.davidnardya.dvsocial.model

import com.google.gson.annotations.SerializedName

data class UserComment(
    val text: String? = "",
    @SerializedName("is-liked")
    override var isLiked: Boolean? = false,
    override var likes: Int? = 0,
    val username: String? = ""
) : Likeable
