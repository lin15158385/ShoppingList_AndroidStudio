package com.example.composableui.model

import com.example.composableui.model.Article
data class ManagedArticleList(
    val id: Int,
    val name: String,
    val articles: List<Article>
)