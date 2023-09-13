package com.davidnardya.dvsocial.utils

import android.widget.Toast
import com.davidnardya.dvsocial.MyApp

fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApp.instance, message, length).show()
}

fun showLikesText(likes: Int): String {
    return if (likes == 1) "$likes like" else "$likes likes"
}
