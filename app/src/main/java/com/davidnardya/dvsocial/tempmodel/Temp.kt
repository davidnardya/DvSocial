package com.davidnardya.dvsocial.tempmodel


import com.google.gson.annotations.SerializedName

data class Temp(
    @SerializedName("userList")
    val userList: List<User>
)