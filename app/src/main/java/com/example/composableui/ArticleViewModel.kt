package com.example.composableui

import androidx.lifecycle.ViewModel
import com.example.composableui.data.ArticleRepository
import com.example.composableui.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArticleViewModel(
    private val repository: ArticleRepository = ArticleRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState>(UiState.Loading)

    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        _uiState.value = UiState.Loading

        repository.fetchProducts { result ->
            _uiState.value = result.fold(
                onSuccess = { articles ->
                    if (articles.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(articles)
                    }
                },
                onFailure = {
                    UiState.Error
                }
            )
        }
    }
}
