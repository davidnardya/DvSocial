package com.davidnardya.dvsocial.model

import com.davidnardya.dvsocial.utils.Constants
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@IgnoreExtraProperties
data class UserPost (
    var id: String? = "",
    @SerializedName("image-url")
    val imageUrl: String? = "",
    val caption: String? = "",
    val comments: List<UserComment>? = emptyList(),
    override var likes: List<String>? = emptyList(),
    var username: String? = ""
) : Likeable {
    override fun isLiked(): Boolean = likes?.contains(Constants.currentUser?.id.toString()) == true
}