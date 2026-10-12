
package com.example.composableui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composableui.data.sampleArticles
import com.example.composableui.model.Article
import com.example.composableui.ui.screens.ArticleListScreen
import com.example.composableui.ui.screens.CScreen
import com.example.composableui.ui.screens.CartScreen
import com.example.composableui.ui.screens.CounterScreen
import com.example.composableui.ui.screens.DetailScreen
import com.example.composableui.ui.state.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var showCounter by remember {
                mutableStateOf(false)
            }

            var count by remember {
                mutableStateOf(0)
            }

            var showArticles by remember {
                mutableStateOf(false)
            }

            var selectedArticle by remember {
                mutableStateOf<Article?>(null)
            }

            var cart by remember {
                mutableStateOf(listOf<Article>())
            }

            var showCart by remember {
                mutableStateOf(false)
            }

            var reloadKey by remember {
                mutableIntStateOf(0)
            }

            var uiState by remember {
                mutableStateOf<UiState>(UiState.Empty)
            }

            // Carregar os artigos sempre que abre a lista
            // ou quando o utilizador tenta novamente.
            LaunchedEffect(showArticles, reloadKey) {

                if (showArticles) {

                    uiState = UiState.Loading

                    try {
                        val articles = withContext(Dispatchers.IO) {
                            sampleArticles
                        }

                        uiState = if (articles.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(articles)
                        }

                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        uiState = UiState.Error
                    }
                }
            }

            when {

                // Ecrã do contador
                showCounter -> {
                    CounterScreen(
                        count = count,
                        onIncrement = {
                            count++
                        },
                        onBack = {
                            showCounter = false
                        }
                    )
                }

                // Menu principal
                !showArticles -> {
                    CScreen(
                        onOpenArticles = {
                            showArticles = true
                        },
                        onOpenCounter = {
                            showCounter = true
                        }
                    )
                }

                // Detalhes do artigo selecionado
                selectedArticle != null -> {
                    DetailScreen(
                        article = selectedArticle!!,
                        onBack = {
                            selectedArticle = null
                        }
                    )
                }

                // Estados da lista de artigos
                else -> {
                    when (val state = uiState) {

                        is UiState.Loading -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()

                                Text(
                                    text = "A carregar artigos...",
                                    modifier = Modifier.padding(16.dp)
                                )

                                Button(
                                    onClick = {
                                        showArticles = false
                                    }
                                ) {
                                    Text("Voltar ao menu")
                                }
                            }
                        }

                        // Erro
                        is UiState.Error -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Ocorreu um erro ao carregar os artigos.")

                                Button(
                                    onClick = {
                                        reloadKey++
                                    },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("Tentar novamente")
                                }

                                Button(
                                    onClick = {
                                        showArticles = false
                                    },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("Voltar ao menu")
                                }
                            }
                        }

                        // Lista vazia
                        is UiState.Empty -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Nenhum artigo encontrado.")

                                Button(
                                    onClick = {
                                        reloadKey++
                                    },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("Tentar novamente")
                                }

                                Button(
                                    onClick = {
                                        showArticles = false
                                    },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("Voltar ao menu")
                                }
                            }
                        }

                        //  Carregamento bem-sucedido
                        is UiState.Success -> {
                            Box {

                                ArticleListScreen(
                                    articles = state.articles,

                                    onArticleClick = { article ->
                                        selectedArticle = article
                                    },

                                    onAddToCart = { article ->
                                        cart = cart + article
                                    }
                                )

                                Button(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(16.dp),

                                    onClick = {
                                        showCart = true
                                    }
                                ) {
                                    Text("🛒 Cesto (${cart.size})")
                                }

                                Button(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp),

                                    onClick = {
                                        showArticles = false
                                    }
                                ) {
                                    Text("← Menu")
                                }
                            }
                        }
                    }
                }
            }

            // Cesto de compras
            if (showCart) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showCart = false
                    }
                ) {
                    CartScreen(
                        cart = cart,

                        onPreview = { article ->
                            selectedArticle = article
                            showCart = false
                        },

                        onRemove = { article ->
                            cart = cart - article
                        }
                    )
                }
            }
        }
    }
}
