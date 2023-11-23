package com.davidnardya.dvsocial.repositories

import android.net.Uri
import android.util.Log
import com.davidnardya.dvsocial.api.UserApi
import com.davidnardya.dvsocial.events.UserEvents
import com.davidnardya.dvsocial.model.DvUser
import com.davidnardya.dvsocial.model.UserPost
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.utils.Constants.DID_LOG_IN
import com.davidnardya.dvsocial.utils.Constants.PASSWORD
import com.davidnardya.dvsocial.utils.Constants.USER_ID
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
) {
    private val dBRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val imageDBRef = FirebaseStorage.getInstance()


    var eventsFlow: MutableSharedFlow<UserEvents>? = null

    private val userList = MutableStateFlow(mutableListOf<DvUser>())
    fun getUserListFlow(): MutableStateFlow<MutableList<DvUser>> = userList

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
        val username = getStringFromPreferenceDataStore(USER_NAME)
        val password = getStringFromPreferenceDataStore(PASSWORD)
        eventsFlow?.tryEmit(UserEvents.OnLogIn(username,password))
    }

    suspend fun saveLoggedInUser(user: DvUser) {
        Log.d("123321","saveLoggedInUser username ${user.username}")
        user.username?.let { userPreferencesDataStore.savePreferencesDataStoreValues(USER_NAME, it) }
        user.password?.let { userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD, it) }
        user.id?.let { userPreferencesDataStore.savePreferencesDataStoreValues(USER_ID, it) }
        saveIsUserLoggedIn(true)
        Constants.currentUser = user
    }

    suspend fun registerNewUser(username: String, password: String) {
        if (username != "" && password != "") {
            val database = Firebase.database
            val myRef = database.getReference("userList")

            myRef.push().let {
                it.setValue(
                    DvUser(it.key, username, password)
                )
                userPreferencesDataStore.savePreferencesDataStoreValues(USER_ID, it.key.toString())
            }
            userPreferencesDataStore.savePreferencesDataStoreValues(USER_NAME, username)
            userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD, password)
        }
    }

    suspend fun logUserOut() {
        userPreferencesDataStore.savePreferencesDataStoreValues(USER_NAME, "")
        userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD, "")
        userPreferencesDataStore.savePreferencesDataStoreValues(USER_ID, "")
        saveIsUserLoggedIn(false)
        clearDataStore()
    }

    private suspend fun clearDataStore() {
        userPreferencesDataStore.removeKey(USER_NAME)
        userPreferencesDataStore.removeKey(PASSWORD)
        userPreferencesDataStore.removeKey(USER_ID)
        userPreferencesDataStore.removeKey(DID_LOG_IN)
        userPreferencesDataStore.clear()
    }

    suspend fun getStringFromPreferenceDataStore(key: String) : String {
        return userPreferencesDataStore.getPreferencesDataStoreValues(key, "").toString()
    }

    private suspend fun saveIsUserLoggedIn(didLogIn: Boolean) {
        userPreferencesDataStore.savePreferencesDataStoreValues(DID_LOG_IN, didLogIn)
    }

    suspend fun getIsUserLoggedIn(): Boolean {
        return userPreferencesDataStore.getPreferencesDataStoreValues(DID_LOG_IN, false) == true &&
                userPreferencesDataStore.getPreferencesDataStoreValues(USER_NAME, "") != "" &&
                userPreferencesDataStore.getPreferencesDataStoreValues(USER_NAME, "") != "null" &&
                userPreferencesDataStore.getPreferencesDataStoreValues(PASSWORD, "") != "" &&
                userPreferencesDataStore.getPreferencesDataStoreValues(PASSWORD, "") != "null"


    }

    fun uploadImage(uri: Uri): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val storageReference =
            imageDBRef.reference.child("images/${timeStamp}${System.currentTimeMillis()}.jpg")
        storageReference.putFile(uri)
        return storageReference.path
    }

    fun getImageDownloadUrl(path: String, coroutineScope: CoroutineScope) {
        imageDBRef.getReference(path).downloadUrl.addOnSuccessListener {
            coroutineScope.launch {
                imageDownloadUrlProduceResult.send(it)
            }
        }.addOnFailureListener {
            Log.e("UserRepository getImageDownloadUrl", "error: ${it.message}")
        }
    }

    suspend fun uploadNewUserPost(newPost: UserPost, userId: String?, user: DvUser?) {
        Log.d("123321","uploadNewUserPost repo newPost username: ${newPost.username}")
        Log.d("123321","uploadNewUserPost repo user username: ${user?.username}")
        //TODO: Need to find a way to update child instead of deletion and re-uploading
        if (userId == "null" || userId == null) {
            return
        }
        val database = Firebase.database
        val myRef = database.getReference("userList")

        val posts = user?.posts?.toMutableList()
        posts?.add(newPost)

        myRef.child(userId).removeValue()

        myRef.push().let {
            val newUser = DvUser(
                it.key,
                user?.username,
                user?.password,
                posts,
                user?.notifications
            )
            it.setValue(
                newUser
            )
            saveLoggedInUser(newUser)
        }

//        val key = userId?.let {
//            myRef
//                .child(it)
//                .child("posts").push().key
//        }

//        val postValues = newPost.toMap()
//        val childUpdate = hashMapOf<String, Any>(
//            "/$userId/posts/$key" to postValues
//        )
//        val childUpdate = mapOf<String, Any>(
//            "/$userId/posts/$key" to newPost
//        )
//        myRef.updateChildren(childUpdate)

    }
}

val imageDownloadUrlProduceResult = Channel<Uri>()