package com.davidnardya.dvsocial.model

import com.davidnardya.dvsocial.utils.Constants
import com.google.gson.annotations.SerializedName

data class UserComment(
    val text: String? = "",
    @SerializedName("is-liked")
    override var likes: List<String>? = emptyList(),
    val username: String? = "",
    val id: String? = ""
) : Likeable {
    override fun isLiked(): Boolean = likes?.contains(Constants.currentUser?.id.toString()) == true
}
