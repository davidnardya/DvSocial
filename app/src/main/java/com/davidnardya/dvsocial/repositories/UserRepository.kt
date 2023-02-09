package com.davidnardya.dvsocial.repositories

import com.davidnardya.dvsocial.api.UserApi
import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.utils.UserPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.random.Random

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
    ) {
    private val userList = MutableStateFlow(emptyList<User>())



    private suspend fun getUserImage() = userApi.getImage()

    fun getUserListFlow(): Flow<List<User>> = userList

    private suspend fun getRandomUserPostList() : List<UserPost> {
        val randomPostList = mutableListOf<UserPost>()

        for (i in 0..3) {
            randomPostList.add(UserPost(
                postId = "${Random.nextInt(100000000,999999999)}",
                userName = "${Constants.userNameList[Random.nextInt(0,4)]}${Random.nextInt(100,500)}",
                imageUrl = getUserImage(),
                Constants.userImageCaptionList[Random.nextInt(0,5)]
            ))
        }
        return randomPostList
    }

    suspend fun loadUsersToFeed() {
        val userListToSend = mutableListOf<User>()
        for(i in 0..3) {
            userListToSend.add(
                User(
                    userId = "${Random.nextInt(100000000,999999999)}",
                    password = "1122",
                    userName = "${Constants.userNameList[Random.nextInt(0,4)]}${Random.nextInt(100,500)}",
//                    userName = "${java.util.UUID.randomUUID()}",
                    posts = getRandomUserPostList()
                )
            )
        }
        val oldList = userList.value.toMutableList()
        oldList.addAll(userListToSend)
        userList.tryEmit(userListToSend)
    }

    suspend fun saveUserInfo(username: String, password: String) {
        userPreferencesDataStore.savePreferencesDataStoreValues(USER_NAME,username)
        userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD,password)
    }

    suspend fun getUserInfo(): Pair<String, String> {
        return Pair(
            userPreferencesDataStore.getPreferencesDataStoreValues(USER_NAME,"").toString(),
            userPreferencesDataStore.getPreferencesDataStoreValues(PASSWORD,"").toString()
        )
    }

    companion object {
        private const val USER_NAME = "username"
        private const val PASSWORD = "password"
    }
}