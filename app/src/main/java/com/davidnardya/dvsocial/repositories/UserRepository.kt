package com.davidnardya.dvsocial.repositories

import android.util.Log
import com.davidnardya.dvsocial.api.UserApi
import com.davidnardya.dvsocial.model.DvUser
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
    private val userList = MutableStateFlow(mutableListOf<DvUser>())
    private val currentUser = MutableStateFlow(Constants.emptyUser)

    private suspend fun getUserImage() = userApi.getImage()

    fun getUserListFlow(): MutableStateFlow<MutableList<DvUser>> = userList
    fun getCurrentUserFlow(): Flow<DvUser> = currentUser

    private suspend fun getRandomFeedUserPostList() : List<UserPost> {
        val randomPostList = mutableListOf<UserPost>()

        for (i in 0..3) {
            randomPostList.add(UserPost(
                userName = "${Constants.userNameList[Random.nextInt(0,4)]}${Random.nextInt(100,500)}",
                imageUrl = getUserImage(),
                caption = Constants.userImageCaptionList[Random.nextInt(0,5)],
                comments = Constants.mockComments[Random.nextInt(0,3)]
            ))
        }
        return randomPostList
    }

    suspend fun subscribeToUserListFlow() {
        val userListToSend = mutableListOf<DvUser>()
        for(i in 0..3) {
            userListToSend.add(
                DvUser(
                    userName = "${Constants.userNameList[Random.nextInt(0,4)]}${Random.nextInt(100,500)}",
                    password = "1122",
                    posts = getRandomFeedUserPostList(),
                    notifications = Constants.mockNotifications
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

    suspend fun clearDataStore() {
        userPreferencesDataStore.removeKey(USER_NAME)
        userPreferencesDataStore.removeKey(PASSWORD)
        userPreferencesDataStore.removeKey(DID_LOG_IN)
        userPreferencesDataStore.clear()
    }

    suspend fun getUserInfo(): DvUser {
        val userName = userPreferencesDataStore.getPreferencesDataStoreValues(USER_NAME,"").toString()
        val password = userPreferencesDataStore.getPreferencesDataStoreValues(PASSWORD,"").toString()
        Log.d("123321","userName $userName password $password")
        return DvUser(
            userName = userName,
            password = password,
            posts = getRandomFeedUserPostList(),
            notifications = Constants.mockNotifications
        )
    }

    suspend fun saveUserLoggedIn(didLogIn: Boolean) {
        userPreferencesDataStore.savePreferencesDataStoreValues(DID_LOG_IN,didLogIn)
    }

    suspend fun getUserLoggedIn(): Boolean {
        return userPreferencesDataStore.getPreferencesDataStoreValues(DID_LOG_IN,false) == true
    }
}