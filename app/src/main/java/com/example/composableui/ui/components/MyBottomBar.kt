
package com.example.composableui.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.composableui.navigation.NavigationScreen

@Composable
fun MyBottomBar(
    selectedRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedRoute == NavigationScreen.Articles.route,
            onClick = {
                onNavigate(NavigationScreen.Articles.route)
            },
            icon = { Text("▤") },
            label = { Text("Artigos") }
        )

        NavigationBarItem(
            selected = selectedRoute == NavigationScreen.Cart.route,
            onClick = {
                onNavigate(NavigationScreen.Cart.route)
            },
            icon = { Text("🛒") },
            label = { Text("Carrinho") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                onNavigate(NavigationScreen.Home.route)
            },
            icon = { Text("⌂") },
            label = { Text("Menu") }
        )
    }
}
