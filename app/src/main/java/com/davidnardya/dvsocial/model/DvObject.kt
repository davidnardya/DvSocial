package com.davidnardya.dvsocial.model

import com.google.gson.annotations.SerializedName

data class DvObject(
    @SerializedName("userList")
    val userList: List<DvUser>?
)