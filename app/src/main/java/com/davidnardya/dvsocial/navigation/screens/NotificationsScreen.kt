package com.davidnardya.dvsocial.navigation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidnardya.dvsocial.utils.showToast
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import com.davidnardya.dvsocial.viewmodel.LoginViewModel

@Composable
fun NotificationsScreen(loginViewModel: LoginViewModel, navHostController: NavHostController) {
    val notifications = loginViewModel.currentUser.value?.notifications ?: emptyList()
    LazyColumn {
        items(notifications) { notification ->
            Column (
                modifier = Modifier.fillMaxSize(),
            ){
                ClickableText(
                    text = AnnotatedString(notification.text ?: "NoText"),
                    modifier = Modifier.padding(6.dp),
                    onClick = {
                        showToast("Coming soon!")
                    }
                )
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .height(1.dp)
                        .padding(horizontal = 6.dp)
                )
            }
        }
    }
}