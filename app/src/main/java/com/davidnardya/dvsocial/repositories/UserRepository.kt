package com.davidnardya.dvsocial.repositories

import android.net.Uri
import android.util.Log
import com.davidnardya.dvsocial.api.UserApi
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
) {
    private val dBRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val imageDBRef = FirebaseStorage.getInstance().reference


    private val userList = MutableStateFlow(mutableListOf<DvUser>())
    fun getUserListFlow(): MutableStateFlow<MutableList<DvUser>> = userList

    private val currentUserFlow = MutableStateFlow(Constants.emptyUser)
    fun getCurrentUserFlow(): Flow<DvUser> = currentUserFlow


    fun subscribeToUserListFlow() {
        dBRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<DvUser>()
                snapshot.children.forEach { users ->
                    users.children.forEach { user ->
                        user.getValue<DvUser>()?.let {
                            list.add(it)
                        }
                    }
                }
                userList.tryEmit(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserRepository ValueEventListener", error.message)
            }
        })

    }

    suspend fun subscribeToCurrentUserFlow() {
        delay(1000)
        currentUserFlow.tryEmit(getUserInfo())
    }

    suspend fun saveUserInfo(username: String, password: String, isNewUser: Boolean = false) {

        if (username != "" && password != "") {

            if(isNewUser) {
                val database = Firebase.database
                val myRef = database.getReference("userList")

                myRef.push().let {
                    it.setValue(
                        DvUser(
                            it.key,
                            username,
                            password,
                            Constants.mockPosts,
                            Constants.mockNotifications
                        )
                    )
                }
            }
            userPreferencesDataStore.savePreferencesDataStoreValues(USER_NAME, username)
            userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD, password)
        } else {
            userPreferencesDataStore.savePreferencesDataStoreValues(USER_NAME, username)
            userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD, password)
        }


    }

    suspend fun clearDataStore() {
        userPreferencesDataStore.removeKey(USER_NAME)
        userPreferencesDataStore.removeKey(PASSWORD)
        userPreferencesDataStore.removeKey(DID_LOG_IN)
        userPreferencesDataStore.clear()
    }

    suspend fun getUserInfo(): DvUser {
        val userName =
            userPreferencesDataStore.getPreferencesDataStoreValues(USER_NAME, "").toString()
        val password =
            userPreferencesDataStore.getPreferencesDataStoreValues(PASSWORD, "").toString()
        Log.d("123321", "userName $userName password $password")
        return DvUser(
            username = userName,
            password = password,
            posts = /*userList.value[0].posts ?:*/ Constants.mockPosts,
            notifications = Constants.mockNotifications
        )
    }

    suspend fun saveUserLoggedIn(didLogIn: Boolean) {
        userPreferencesDataStore.savePreferencesDataStoreValues(DID_LOG_IN, didLogIn)
    }

    suspend fun getUserLoggedIn(): Boolean {
        return userPreferencesDataStore.getPreferencesDataStoreValues(DID_LOG_IN, false) == true
    }

    fun uploadImage(uri: Uri): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val storageReference = imageDBRef.child("images/${timeStamp}${System.currentTimeMillis()}.jpg")
        storageReference.putFile(uri)
        return storageReference.path
    }

    suspend fun getImageDownloadUrl(path: String): String {
        var result = ""

        return result
    }
}