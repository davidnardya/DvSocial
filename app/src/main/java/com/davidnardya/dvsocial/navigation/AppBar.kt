package com.davidnardya.dvsocial.navigation

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.davidnardya.dvsocial.R

@Composable
fun AppBar(
    onNavigationItemClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name)
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon = {
            IconButton(onClick = onNavigationItemClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Toggle Drawer")
            }
        }
    )
}