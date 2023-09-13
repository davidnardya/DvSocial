package com.davidnardya.dvsocial.repositories

import com.davidnardya.dvsocial.api.UserApi
import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.utils.Constants.DID_LOG_IN
import com.davidnardya.dvsocial.utils.Constants.PASSWORD
import com.davidnardya.dvsocial.utils.Constants.USER_NAME
import com.davidnardya.dvsocial.utils.UserPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.random.Random

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
    ) {
    private val userList = MutableStateFlow(mutableListOf<User>())
    private val currentUser = MutableStateFlow(Constants.emptyUser)

    private suspend fun getUserImage() = userApi.getImage()

    fun getUserListFlow(): MutableStateFlow<MutableList<User>> = userList
    fun getCurrentUserFlow(): Flow<User> = currentUser

    private suspend fun getRandomUserPostList() : List<UserPost> {
        val randomPostList = mutableListOf<UserPost>()

        for (i in 0..3) {
            randomPostList.add(UserPost(
                id = "${Random.nextInt(100000000,999999999)}",
                userName = "${Constants.userNameList[Random.nextInt(0,4)]}${Random.nextInt(100,500)}",
                imageUrl = getUserImage(),
                caption = Constants.userImageCaptionList[Random.nextInt(0,5)],
                comments = Constants.mockComments[Random.nextInt(0,3)]
            ))
        }
        return randomPostList
    }

    suspend fun subscribeToUserListFlow() {
        val userListToSend = mutableListOf<User>()
        for(i in 0..3) {
            userListToSend.add(
                User(
                    userId = "${Random.nextInt(100000000,999999999)}",
                    userName = "${Constants.userNameList[Random.nextInt(0,4)]}${Random.nextInt(100,500)}",
//                    userName = "${java.util.UUID.randomUUID()}",
                    password = "1122",
                    posts = getRandomUserPostList()
                )
            )
        }
        val oldList = userList.value.toMutableList()
        oldList.addAll(userListToSend)
        userList.tryEmit(userListToSend)
    }

    suspend fun subscribeToCurrentUserFlow() {
        currentUser.tryEmit(getUserInfo())
    }

    suspend fun saveUserInfo(username: String, password: String) {
        userPreferencesDataStore.savePreferencesDataStoreValues(USER_NAME,username)
        userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD,password)
    }

    suspend fun getUserInfo(): User {
        return User(
            userId = "${Random.nextInt(100000000,999999999)}",
            userName = userPreferencesDataStore.getPreferencesDataStoreValues(USER_NAME,"").toString(),
            password = userPreferencesDataStore.getPreferencesDataStoreValues(PASSWORD,"").toString(),
            posts = emptyList()
        )
    }

    suspend fun saveUserLoggedIn(didLogIn: Boolean) {
        userPreferencesDataStore.savePreferencesDataStoreValues(DID_LOG_IN,didLogIn)
    }

    suspend fun getUserLoggedIn(): Boolean {
        return userPreferencesDataStore.getPreferencesDataStoreValues(DID_LOG_IN,false) == true
    }
}