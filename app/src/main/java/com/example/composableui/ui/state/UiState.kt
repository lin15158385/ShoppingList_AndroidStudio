package com.example.composableui.ui.state

import com.example.composableui.model.Article

sealed class UiState {

    object Loading : UiState()

    object Error : UiState()

    object Empty : UiState()

    data class Success(
        val articles: List<Article>
    ) : UiState()
}