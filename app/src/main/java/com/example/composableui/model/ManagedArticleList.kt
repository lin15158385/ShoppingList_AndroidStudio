package com.example.composableui.model

import com.example.composableui.model.Article

// uma nova class de data
// Article.kt — representa um artigo individual
// ManagedArticleList.kt — representa uma lista de artigos
data class ManagedArticleList(
    val id: Int,
    val name: String,
    val articles: List<Article>
)