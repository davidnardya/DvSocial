package com.davidnardya.dvsocial.utils

import android.widget.Toast
import com.davidnardya.dvsocial.MyApp

    fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(MyApp.instance,message,length).show()
    }
