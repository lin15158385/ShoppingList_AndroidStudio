
package com.example.composableui.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title: String,
    navController: NavController,
    isBaseScreen: Boolean,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            if (!isBaseScreen) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text("←")
                }
            }
        },
        actions = {
            actions()
        }
    )
}
