
package com.example.composableui

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composableui.data.sampleArticles
import com.example.composableui.model.Article
import com.example.composableui.navigation.Screen
import com.example.composableui.ui.screens.ArticleListScreen
import com.example.composableui.ui.screens.CScreen
import com.example.composableui.ui.screens.CartScreen
import com.example.composableui.ui.screens.CounterScreen
import com.example.composableui.ui.screens.DetailScreen
import com.example.composableui.ui.screens.ListEditorScreen
import com.example.composableui.model.ManagedArticleList
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.composableui.ui.state.UiState
import kotlinx.coroutines.CancellationException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    var selectedBottomRoute by rememberSaveable {
        mutableStateOf(Screen.Articles.route)
    }

    var count by rememberSaveable {
        mutableStateOf(0)
    }

    var cart by remember {
        mutableStateOf(listOf<Article>())
    }

    var selectedArticle by remember {
        mutableStateOf<Article?>(null)
    }

    var selectedListId by rememberSaveable {
        mutableStateOf(1)
    }

    var articleLists by remember {
        mutableStateOf(
            listOf(
                ManagedArticleList(
                    id = 1,
                    name = "Lista 1",
                    articles = sampleArticles
                )
            )
        )
    }

    var reloadKey by remember {
        mutableIntStateOf(0)
    }

    var uiState by remember {
        mutableStateOf<UiState>(UiState.Empty)
    }

    var isBookmarked by rememberSaveable {
        mutableStateOf(false)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    LaunchedEffect(currentRoute, selectedListId, articleLists, reloadKey) {
        if (currentRoute == Screen.Articles.route) {
            uiState = UiState.Loading

            try {
                val articles = articleLists
                    .firstOrNull { it.id == selectedListId }
                    ?.articles
                    .orEmpty()

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

    LaunchedEffect(currentRoute) {
        if (
            currentRoute == Screen.ManageLists.route ||
            currentRoute == Screen.Articles.route ||
            currentRoute == Screen.Cart.route
        ) {
            selectedBottomRoute = currentRoute
        }
    }

    val screens = listOf(
        Screen.Home,
        Screen.Articles,
        Screen.ManageLists,
        Screen.ListEditor,
        Screen.ArticleDetail,
        Screen.Counter,
        Screen.Cart
    )

    val currentScreen = screens.firstOrNull {
        it.route == currentRoute
    } ?: Screen.Home

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(currentScreen.title)
                },
                navigationIcon = {
                    if (!currentScreen.isBaseScreen) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Text("←")
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "A minha aplicação ShoppingList"
                                )
                            }

                            context.startActivity(
                                Intent.createChooser(intent, "Partilhar")
                            )
                        }
                    ) {
                        Text("↗")
                    }

                    IconButton(
                        onClick = {
                            isBookmarked = !isBookmarked
                        }
                    ) {
                        Text(if (isBookmarked) "★" else "☆")
                    }
                }
            )
        },
        bottomBar = {
            if (
                currentScreen.isBaseScreen &&
                currentRoute != Screen.Home.route
            ) {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedBottomRoute ==
                                Screen.ManageLists.route,
                        onClick = {
                            selectedBottomRoute = Screen.ManageLists.route
                            navController.navigate(Screen.ManageLists.route) {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("☷") },
                        label = { Text("Listas") }
                    )

                    NavigationBarItem(
                        selected = selectedBottomRoute ==
                                Screen.Articles.route,
                        onClick = {
                            selectedBottomRoute = Screen.Articles.route
                            navController.navigate(Screen.Articles.route) {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("▤") },
                        label = { Text("Artigos") }
                    )

                    NavigationBarItem(
                        selected = selectedBottomRoute ==
                                Screen.Cart.route,
                        onClick = {
                            selectedBottomRoute = Screen.Cart.route
                            navController.navigate(Screen.Cart.route) {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("🛒") },
                        label = { Text("Carrinho") }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                CScreen(
                    onOpenArticles = {
                        selectedBottomRoute = Screen.Articles.route
                        navController.navigate(Screen.Articles.route)
                    },
                    onOpenCounter = {
                        navController.navigate(Screen.Counter.route)
                    },
                    onManageLists = {
                        selectedBottomRoute = Screen.ManageLists.route
                        navController.navigate(Screen.ManageLists.route)
                    }
                )
            }


            composable(Screen.Articles.route) {
                when (val state = uiState) {
                    is UiState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Text(
                                "A carregar artigos...",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    is UiState.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Ocorreu um erro ao carregar os artigos.")

                            Button(onClick = { reloadKey++ }) {
                                Text("Tentar novamente")
                            }
                        }
                    }

                    is UiState.Empty -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Nenhum artigo encontrado.")

                            Button(onClick = { reloadKey++ }) {
                                Text("Tentar novamente")
                            }
                        }
                    }

                    is UiState.Success -> {
                        ArticleListScreen(
                            articles = state.articles,
                            onArticleClick = { article ->
                                selectedArticle = article
                                navController.navigate(Screen.ArticleDetail.route)
                            },
                            onAddToCart = { article ->
                                cart = cart + article
                            }
                        )
                    }
                }
            }


            composable(Screen.ArticleDetail.route) {
                val article = selectedArticle

                if (article != null) {
                    DetailScreen(
                        article = article,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text("Voltar")
                    }
                }
            }

            composable(Screen.ManageLists.route) {
                androidx.compose.foundation.layout.Column {
                    articleLists.forEach { list ->
                        Text("${list.name} (ID: ${list.id})")

                        Button(
                            onClick = {
                                selectedListId = list.id
                                navController.navigate(
                                    Screen.ListEditor.route
                                )
                            }
                        ) {
                            Text("Abrir lista")
                        }
                    }
                }
            }

            composable(Screen.ListEditor.route) {
                val currentList = articleLists.firstOrNull {
                    it.id == selectedListId
                }

                if (currentList != null) {
                    ListEditorScreen(
                        articleList = currentList,
                        onBack = {
                            navController.popBackStack()
                        },
                        onAddArticle = { article ->
                            articleLists = articleLists.map { list ->
                                if (list.id == selectedListId) {
                                    list.copy(
                                        articles = list.articles + article
                                    )
                                } else {
                                    list
                                }
                            }
                        },
                        onUpdateArticle = { updatedArticle ->
                            articleLists = articleLists.map { list ->
                                if (list.id == selectedListId) {
                                    list.copy(
                                        articles = list.articles.map { article ->
                                            if (article.id == updatedArticle.id) {
                                                updatedArticle
                                            } else {
                                                article
                                            }
                                        }
                                    )
                                } else {
                                    list
                                }
                            }
                        },
                        onDeleteArticle = { articleToDelete ->
                            articleLists = articleLists.map { list ->
                                if (list.id == selectedListId) {
                                    list.copy(
                                        articles = list.articles.filterNot {
                                            it.id == articleToDelete.id
                                        }
                                    )
                                } else {
                                    list
                                }
                            }
                        }
                    )
                }
            }

            composable(Screen.Counter.route) {
                CounterScreen(
                    count = count,
                    onIncrement = {
                        count++
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    cart = cart,
                    onPreview = { article ->
                        selectedArticle = article
                        navController.navigate(Screen.ArticleDetail.route)
                    },
                    onRemove = { article ->
                        cart = cart - article
                    }
                )
            }
        }
    }
}
