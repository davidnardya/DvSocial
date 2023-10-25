package com.davidnardya.dvsocial.api

import com.davidnardya.dvsocial.model.DvUser
import retrofit2.http.GET

interface UserApi {
    @GET("user-list.json")
    suspend fun getUserList(): List<DvUser>
}