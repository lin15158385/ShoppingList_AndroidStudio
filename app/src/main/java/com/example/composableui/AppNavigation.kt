
package com.example.composableui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composableui.data.sampleArticles
import com.example.composableui.model.Article
import com.example.composableui.model.ManagedArticleList
import com.example.composableui.navigation.NavigationScreen
import com.example.composableui.ui.screens.ArticleListScreen
import com.example.composableui.ui.screens.CScreen
import com.example.composableui.ui.screens.CartScreen
import com.example.composableui.ui.screens.CounterScreen
import com.example.composableui.ui.screens.DetailScreen
import com.example.composableui.ui.screens.ListEditorScreen
import com.example.composableui.ui.state.UiState
import kotlinx.coroutines.CancellationException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val articleViewModel: ArticleViewModel = viewModel()

    val apiUiState by articleViewModel.uiState
        .collectAsStateWithLifecycle()

    var selectedBottomRoute by rememberSaveable {
        mutableStateOf(NavigationScreen.Home.route)
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

    var selectedListId by rememberSaveable {//- Preservar selecção da bottom bar com `rememberSaveable`
        mutableStateOf(1)
    }

    var articleLists by remember {
        mutableStateOf(//Manualmente a adicionar , da trabalho a implementar
            listOf(
                ManagedArticleList(
                    id = 1,
                    name = "Lista 1",
                    articles = sampleArticles
                ),
                ManagedArticleList(
                    id = 2,
                    name = "Produtos da API",
                    articles = emptyList()
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


    LaunchedEffect(
        currentRoute,
        selectedListId,
        articleLists,
        reloadKey,
        apiUiState
    ) {
        if (currentRoute == NavigationScreen.Articles.route) {
            try {
                val articles = articleLists
                    .firstOrNull { it.id == selectedListId }
                    ?.articles
                    .orEmpty()

                uiState = when {
                    articles.isNotEmpty() -> {
                        UiState.Success(articles)
                    }

                    selectedListId == 2 &&
                            apiUiState is UiState.Loading -> {
                        UiState.Loading
                    }

                    selectedListId == 2 &&
                            apiUiState is UiState.Error -> {
                        UiState.Error
                    }

                    else -> {
                        UiState.Empty
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                uiState = UiState.Error
            }
        }
    }


    LaunchedEffect(apiUiState) {
        if (apiUiState is UiState.Success) {
            val apiArticles =
                (apiUiState as UiState.Success).articles

            articleLists = articleLists.map { list ->
                if (list.id == 2) {
                    list.copy(articles = apiArticles)
                } else {
                    list
                }
            }
        }
    }

    LaunchedEffect(currentRoute) {
        if (
            currentRoute == NavigationScreen.ManageLists.route ||
            currentRoute == NavigationScreen.Articles.route ||
            currentRoute == NavigationScreen.Cart.route
        ) {
            selectedBottomRoute = currentRoute
        }
    }

    val navigationScreens = listOf(
        NavigationScreen.Home,
        NavigationScreen.Articles,
        NavigationScreen.ManageLists,
        NavigationScreen.ListEditor,
        NavigationScreen.ArticleDetail,
        NavigationScreen.Counter,
        NavigationScreen.Cart
    )

    val currentNavigationScreen = navigationScreens.firstOrNull {
        it.route == currentRoute
    } ?: NavigationScreen.Home

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(currentNavigationScreen.title)
                },
                navigationIcon = {
                    if (!currentNavigationScreen.isBaseScreen) {
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
                currentNavigationScreen.isBaseScreen &&
                currentRoute != NavigationScreen.Home.route
            ) {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedBottomRoute ==
                                NavigationScreen.ManageLists.route,
                        onClick = {
                            selectedBottomRoute = NavigationScreen.ManageLists.route
                            navController.navigate(NavigationScreen.ManageLists.route) {
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
                                NavigationScreen.Articles.route,
                        onClick = {
                            selectedBottomRoute = NavigationScreen.Articles.route
                            navController.navigate(NavigationScreen.Articles.route) {
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
                                NavigationScreen.Cart.route,
                        onClick = {
                            selectedBottomRoute = NavigationScreen.Cart.route
                            navController.navigate(NavigationScreen.Cart.route) {
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
    )
    { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = NavigationScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationScreen.Home.route) {
                CScreen(
                    onOpenArticles = {
                        selectedBottomRoute = NavigationScreen.ManageLists.route
                        navController.navigate(NavigationScreen.ManageLists.route)
                    },
                    onOpenCounter = {
                        navController.navigate(NavigationScreen.Counter.route)
                    }
                )
            }


            composable(NavigationScreen.Articles.route) {

                when (val state = uiState) {

                    is UiState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Text(
                                "A carregar produto...",
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
                            Text("Não foi possível carregar o produto.")

                            Button(
                                onClick = {
                                    articleViewModel.loadProducts()
                                }
                            ) {
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
                            Text("Nenhum produto encontrado.")

                            Button(
                                onClick = {
                                    articleViewModel.loadProducts()
                                }
                            ) {
                                Text("Tentar novamente")
                            }
                        }
                    }

                    is UiState.Success -> {
                        ArticleListScreen(
                            articles = state.articles,
                            onArticleClick = { article ->
                                selectedArticle = article
                                navController.navigate(
                                    NavigationScreen.ArticleDetail.route
                                )
                            }
                        )
                    }
                }

            }


            composable(NavigationScreen.ArticleDetail.route) {
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


            composable(NavigationScreen.ManageLists.route) {
                Column {
                    articleLists.forEach { list ->
                        Text("${list.name} (ID: ${list.id})")

                        Button(
                            onClick = {
                                selectedListId = list.id
                                navController.navigate(NavigationScreen.Articles.route)
                            }
                        ) {
                            Text("Ver artigos")
                        }

                        Button(
                            onClick = {
                                selectedListId = list.id
                                navController.navigate(NavigationScreen.ListEditor.route)
                            }
                        ) {
                            Text("Editar lista")
                        }
                    }
                }
            }


            composable(NavigationScreen.ListEditor.route) {
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

            composable(NavigationScreen.Counter.route) {
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

            composable(NavigationScreen.Cart.route) {
                CartScreen(
                    cart = cart,
                    onPreview = { article ->
                        selectedArticle = article
                        navController.navigate(NavigationScreen.ArticleDetail.route)
                    },
                    onRemove = { article ->
                        cart = cart - article
                    }
                )
            }
        }
    }
}
