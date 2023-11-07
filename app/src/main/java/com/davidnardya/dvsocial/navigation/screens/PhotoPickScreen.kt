package com.davidnardya.dvsocial.navigation.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.davidnardya.dvsocial.BuildConfig
import com.davidnardya.dvsocial.utils.createImageFile
import com.davidnardya.dvsocial.utils.showToast
import java.io.File
import java.io.FileOutputStream
import java.util.Objects

@Composable
fun PhotoPickScreen() {

    //Open camera variables
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }


    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            showToast("Permission Granted")
            cameraLauncher.launch(uri)
        } else {
            showToast("Permission Denied")
        }
    }

    //Gallery pick variables
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
        }
    )

    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                // Request a permission
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text(text = "Capture image from camera")
        }

        Button(
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text(text = "Pick photo from gallery")
        }
    }

    if (capturedImageUri.path?.isNotEmpty() == true) {
        saveImageToInternalStorage(context,capturedImageUri)
    }


}

fun saveImageToInternalStorage(context: Context, uri: Uri) {

    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,uri)
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val saveDir = context.externalCacheDir
    val filePath = File(saveDir, fileName).absolutePath

    val out = FileOutputStream(filePath)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    out.flush()
    out.close()

    val imageUri = MediaStore.Images.Media.insertImage(context.contentResolver, filePath, fileName, "Saved image")
}