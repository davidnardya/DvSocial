package com.davidnardya.dvsocial.utils

import android.content.Context
import android.widget.Toast
import com.davidnardya.dvsocial.MyApp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApp.instance, message, length).show()
}

fun showLikesText(likes: Int): String {
    return if (likes == 1) "$likes like" else "$likes likes"
}

fun String.cleanSpaces(): String {
    var result = this
    while (result.contains("\n\n") || result.contains("  ")) {
        result = result.replace("\n\n", "\n")
        result = result.replace("  ", " ")
    }
    return result
}

fun Context.createImageFile(): File {
    // Create an image file name
//    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val timeStamp = SimpleDateFormat("yyyy,MM,dd_HH:mm:ss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}
