
package com.example.composableui.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.composableui.model.Article
import com.example.composableui.ui.components.RowArticle

@Composable
fun ArticleListScreen(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    onAddToCart: (Article) -> Unit
) {
    LazyColumn {
        items(articles) { article ->
            RowArticle(
                article = article,
                onPreview = {
                    onArticleClick(article)
                },
                onAddToCart = {
                    onAddToCart(article)
                }
            )
        }
    }
}
