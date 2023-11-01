package com.davidnardya.dvsocial.api

import com.davidnardya.dvsocial.model.DvObject
import com.davidnardya.dvsocial.model.DvUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET("userList.json")
    suspend fun getUserList(): DvObject

    @POST("userList.json")
    suspend fun registerUser(@Body newUser: DvUser): Response<DvUser>
}