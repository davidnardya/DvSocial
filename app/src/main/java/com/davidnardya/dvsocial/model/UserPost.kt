package com.davidnardya.dvsocial.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@IgnoreExtraProperties
data class UserPost (
    var id: String? = "",
    @SerializedName("image-url")
    val imageUrl: String? = "",
    val caption: String? = "",
    val comments: List<UserComment>? = emptyList(),
    @SerializedName("is-liked")
    override var isLiked: Boolean? = false,
    override var likes: Int? = 0,
    var username: String? = ""
) : Likeable