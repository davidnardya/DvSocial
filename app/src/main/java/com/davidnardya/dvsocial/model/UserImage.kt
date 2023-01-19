package com.davidnardya.dvsocial.model

import com.google.gson.annotations.SerializedName

data class UserImage(
    @SerializedName("message")
    val image: String,
    @SerializedName("status")
    val status: String
)