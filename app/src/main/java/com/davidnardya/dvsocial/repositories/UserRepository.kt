package com.davidnardya.dvsocial.repositories

import android.util.Log
import com.davidnardya.dvsocial.api.UserApi
import com.davidnardya.dvsocial.model.DvObject
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.utils.Constants.DID_LOG_IN
import com.davidnardya.dvsocial.utils.Constants.PASSWORD
import com.davidnardya.dvsocial.utils.Constants.USER_NAME
import com.davidnardya.dvsocial.utils.UserPreferencesDataStore
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
    ) {
    private var dBRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    private suspend fun getUserList(): List<DvUser>? {
//        return userApi.getUserList()?.userList
//        var userList: MutableList<DvUser> = mutableListOf()
        var userList: MutableList<DvUser> = mutableListOf()
        dBRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("123321","onDataChange")

//                snapshot.getValue<List<DvUser>>()?.let {
//                    userList = it
//                }

                snapshot.children.forEach {
                    it.children.forEach { user ->
                        val i = user.getValue<DvUser>()
                        Log.d("123321","it.value ${user.value}")
                        Log.d("123321","i username ${i?.username}")
                        user.getValue<DvUser>()?.let { it1 -> userList.add(it1) }
                    }

                }
                Log.d("123321","userList $userList")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserRepository ValueEventListener", error.message)
            }

        })
        return userList
    }


    private val userList = MutableStateFlow(mutableListOf<DvUser>())
    fun getUserListFlow(): MutableStateFlow<MutableList<DvUser>> = userList

    private val currentUserFlow = MutableStateFlow(Constants.emptyUser)
    fun getCurrentUserFlow(): Flow<DvUser> = currentUserFlow

//    private suspend fun getRandomFeedUserPostList() : List<UserPost> {
//        val randomPostList = mutableListOf<UserPost>()
//
//        for (i in 0..3) {
//            randomPostList.add(UserPost(
//                imageUrl = getUserList(),
//                caption = Constants.userImageCaptionList[Random.nextInt(0,5)],
//                comments = Constants.mockComments[Random.nextInt(0,3)]
//            ))
//        }
//        return randomPostList
//    }



    suspend fun subscribeToUserListFlow() {
//        val userListToSend = mutableListOf<DvUser>()
//        for(i in 0..3) {
//            userListToSend.add(
//                DvUser(
//                    username = "${Constants.userNameList[Random.nextInt(0,4)]}${Random.nextInt(100,500)}",
//                    password = "1122",
//                    posts = getRandomFeedUserPostList(),
//                    notifications = Constants.mockNotifications
//                )
//            )
//        }
//        val oldList = userList.value.toMutableList()
//        oldList.addAll(userListToSend)
//        getUserList()?.userList?.toMutableList()?.let { userList.tryEmit(it) }

        getUserList()?.toMutableList()?.let { userList.tryEmit(it) }

    }

    suspend fun subscribeToCurrentUserFlow() {
        delay(1000)
        currentUserFlow.tryEmit(getUserInfo())
    }

    suspend fun saveUserInfo(username: String, password: String) {

        if(username != "" && password != "") {

            val database = Firebase.database
            val myRef = database.getReference("userList")

            myRef.push().setValue(
                DvUser(
                    username,
                    password,
                    Constants.mockPosts,
                    Constants.mockNotifications
                )
            )
//            val response: Response<DvUser> = userApi.registerUser(
//                DvUser(
//                    username,
//                    password,
//                    Constants.mockPosts,
//                    Constants.mockNotifications
//                )
//            )
//            if (response.isSuccessful) {
//                Log.d("123321","username ${response.body()?.username} response ${response.message()}")
                userPreferencesDataStore.savePreferencesDataStoreValues(USER_NAME,username)
                userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD,password)

//            }
        } else {
            userPreferencesDataStore.savePreferencesDataStoreValues(USER_NAME,username)
            userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD,password)
        }


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
            username = userName,
            password = password,
            posts = /*userList.value[0].posts ?:*/ Constants.mockPosts,
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