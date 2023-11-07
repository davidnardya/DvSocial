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
) : Likeable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "image-url" to imageUrl,
            "caption" to caption,
            "comments" to comments,
            "is-liked" to isLiked,
            "likes" to likes,
            "username" to username
        )
    }
}