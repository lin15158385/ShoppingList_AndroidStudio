
package com.example.composableui.navigation

sealed class NavigationScreen(
    val route: String,
    val title: String,
    val isBaseScreen: Boolean
) {
    object Home : NavigationScreen(
        route = "home",
        title = "Menu",
        isBaseScreen = true
    )

    object Articles : NavigationScreen(
        route = "articles",
        title = "Artigos",
        isBaseScreen = true
    )

    object ManageLists : NavigationScreen(
        route = "manage_lists",
        title = "Gerir listas",
        isBaseScreen = true
    )

    object ListEditor : NavigationScreen(
        route = "list_editor",
        title = "Editar lista",
        isBaseScreen = false
    )

    object ArticleDetail : NavigationScreen(
        route = "article_detail",
        title = "Detalhes do artigo",
        isBaseScreen = false
    )

    object Counter : NavigationScreen(
        route = "counter",
        title = "Contador",
        isBaseScreen = false
    )

    object Cart : NavigationScreen(
        route = "cart",
        title = "Carrinho",
        isBaseScreen = true
    )
}
