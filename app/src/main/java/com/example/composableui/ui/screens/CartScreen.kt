package com.example.composableui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
    if (cart.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("O carrinho está vazio.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = cart,
                key = { it.id }
            ) { article ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Tipo: ${article.type}")

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onPreview(article) }
                            ) {
                                Text("Ver detalhes")
                            }

                            Button(
                                onClick = { onRemove(article) }
                            ) {
                                Text("Remover")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Carrinho com artigos")
@Composable
fun CartScreenPreview() {
    val previewCart = listOf(
        Article(
            id = 1,
            title = "Big Mac",
            type = "Beef/Carne de Vaca",
            content = "Big Mac"
        ),
        Article(
            id = 2,
            title = "McChicken",
            type = "Chicken/Frango",
            content = "McChicken"
        )
    )

    MaterialTheme {
        CartScreen(
            cart = previewCart,
            onPreview = {},
            onRemove = {}
        )
    }
}

@Preview(showBackground = true, name = "Carrinho vazio")
@Composable
fun CartScreenEmptyPreview() {
    MaterialTheme {
        CartScreen(
            cart = emptyList(),
            onPreview = {},
            onRemove = {}
        )
    }
}