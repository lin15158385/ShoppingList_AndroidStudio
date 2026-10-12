package com.example.composableui.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composableui.model.Article

@Composable
fun RowArticle(
    article: Article,
    onPreview: () -> Unit,
    onAddToCart: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(article.title)
            Text(article.type)

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row {

                Button(
                    onClick = onPreview
                ) {
                    Text("Preview")
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Button(
                    onClick = onAddToCart
                ) {
                    Text("Adicionar")
                }
            }
        }
    }
}
