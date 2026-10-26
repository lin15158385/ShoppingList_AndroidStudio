package com.example.composableui.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.composableui.data.sampleArticles
import com.example.composableui.model.Article
import com.example.composableui.ui.components.RowArticle

@Composable
fun ArticleListScreen(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit
) {

    LazyColumn {

        items(articles) { article ->

            RowArticle(
                article = article,

                onPreview = {
                    onArticleClick(article)
                },
            )
        }
    }
}

@Preview(showBackground = true, name = "Lista de artigos")
@Composable
fun ArticleListScreenPreview() {
    ArticleListScreen(
        articles = sampleArticles,
        onArticleClick = {},
    )
}

@Preview(showBackground = true, name = "Lista vazia")
@Composable
fun ArticleListEmptyPreview() {
    ArticleListScreen(
        articles = emptyList(),
        onArticleClick = {},
    )
}