package com.davidnardya.dvsocial.model

import com.google.gson.annotations.SerializedName

data class DvObject(
    @SerializedName("user-list")
    val userList: List<DvUser>? = emptyList()
)