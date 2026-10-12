// CartScreen.kt
package com.example.composableui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composableui.model.Article

@Composable
fun CartScreen(
    cart: List<Article>,
    onPreview: (Article) -> Unit,
    onRemove: (Article) -> Unit
) {

    LazyColumn {

        items(cart) { article ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(article.title)

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Row {

                        Button(
                            onClick = {
                                onPreview(article)
                            }
                        ) {
                            Text("Preview")
                        }

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Button(
                            onClick = {
                                onRemove(article)
                            }
                        ) {
                            Text("Remover")
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Cesto com artigos")
@Composable
fun CartScreenPreview() {
    CartScreen(
        cart = listOf(
            com.example.composableui.model.Article(
                id = 1,
                title = "Big Mac",
                type = "Beef/Carne de Vaca",
                content = "Big Mac"
            ),
            com.example.composableui.model.Article(
                id = 2,
                title = "McChicken",
                type = "Chicken/Frango",
                content = "McChicken"
            )
        ),
        onPreview = {},
        onRemove = {}
    )
}

@Preview(showBackground = true, name = "Cesto vazio")
@Composable
fun CartScreenEmptyPreview() {
    CartScreen(
        cart = emptyList(),
        onPreview = {},
        onRemove = {}
    )
}