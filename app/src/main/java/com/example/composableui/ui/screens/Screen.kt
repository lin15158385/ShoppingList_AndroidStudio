package com.example.composableui.ui.screens
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CScreen(
    onOpenArticles: () -> Unit,
    onOpenCounter: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = onOpenCounter
        ) {
            Text("Contador")
        }

        Button(
            onClick = onOpenArticles
        ) {
            Text("Artigos")
        }
    }
}

@Preview(showBackground = true, name = "Menu principal")
@Composable
fun CScreenPreview() {
    CScreen(
        onOpenArticles = {},
        onOpenCounter = {}
    )
}