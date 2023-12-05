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
    private val userDBRef = Firebase.database.getReference("userList")


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
                Log.e("${this::class.java.simpleName} ValueEventListener", error.message)
            }
        })

    }

    suspend fun subscribeToCurrentUserFlow() {
        val username = getStringFromPreferenceDataStore(USER_NAME)
        val password = getStringFromPreferenceDataStore(PASSWORD)
        eventsFlow?.tryEmit(UserEvents.OnLogIn(username, password))
    }

    suspend fun saveLoggedInUser(user: DvUser) {
        user.username?.let {
            userPreferencesDataStore.savePreferencesDataStoreValues(
                USER_NAME,
                it
            )
        }
        user.password?.let { userPreferencesDataStore.savePreferencesDataStoreValues(PASSWORD, it) }
        user.id?.let { userPreferencesDataStore.savePreferencesDataStoreValues(USER_ID, it) }
        saveIsUserLoggedIn(true)
        Constants.currentUser = user
    }

    suspend fun registerNewUser(username: String, password: String) {
        if (username != "" && password != "") {
            userDBRef.push().let {
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
        Constants.currentUser = null
    }

    private suspend fun clearDataStore() {
        userPreferencesDataStore.removeKey(USER_NAME)
        userPreferencesDataStore.removeKey(PASSWORD)
        userPreferencesDataStore.removeKey(USER_ID)
        userPreferencesDataStore.removeKey(DID_LOG_IN)
        userPreferencesDataStore.clear()
    }

    suspend fun getStringFromPreferenceDataStore(key: String): String {
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
            Log.e("${this::class.java.simpleName} getImageDownloadUrl", "error: ${it.message}")
        }
    }

    fun uploadNewUserPost(newPost: UserPost, userId: String?) {
        if (userId == "null" || userId == null) {
            return
        }
        val posts = userDBRef
            .child(userId)
            .child("posts")

        posts.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val updateMap = HashMap<String, Any>()

                val list = ArrayList<UserPost>()
                if (dataSnapshot.exists()) {
                    // The "posts" list already exists, insert the new item
                    dataSnapshot.children.forEach { existingPost ->
                        existingPost.getValue<UserPost>()?.let {
                            list.add(it)
                        }
                    }
                    list.add(newPost)
                } else {
                    // The "posts" list doesn't exist, create it and insert the new item
                    list.add(newPost)
                }
                updateMap["posts"] = list

                // Use updateChildren to update the specific value within the object
                userDBRef.child(userId).updateChildren(updateMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                Log.e("${this::class.java.simpleName} uploadNewUserPost", databaseError.message)
            }
        })
    }
}

val imageDownloadUrlProduceResult = Channel<Uri>()