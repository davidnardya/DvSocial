package com.davidnardya.dvsocial.api

import com.davidnardya.dvsocial.model.UserImage
import retrofit2.http.GET

interface UserApi {
    @GET("breeds/image/random")
    suspend fun getImage(): UserImage
}