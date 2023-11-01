package com.davidnardya.dvsocial.tempmodel


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)