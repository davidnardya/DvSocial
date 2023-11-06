package com.davidnardya.dvsocial.model

import com.google.gson.annotations.SerializedName

data class UserPost (
    @SerializedName("image-url")
    val imageUrl: String? = "",
    val caption: String? = "",
    val comments: List<UserComment>? = emptyList(),
    @SerializedName("is-liked")
    override var isLiked: Boolean? = false,
    override var likes: Int? = 0,
    var username: String? = ""
) : Likeable