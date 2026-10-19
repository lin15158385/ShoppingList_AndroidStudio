
package com.example.composableui.navigation

sealed class Screen(
    val route: String,
    val title: String,
    val isBaseScreen: Boolean
) {
    object Home : Screen(
        route = "home",
        title = "Menu",
        isBaseScreen = true
    )

    object Articles : Screen(
        route = "articles",
        title = "Artigos",
        isBaseScreen = true
    )

    object ManageLists : Screen(
        route = "manage_lists",
        title = "Gerir listas",
        isBaseScreen = true
    )

    object ListEditor : Screen(
        route = "list_editor",
        title = "Editar lista",
        isBaseScreen = false
    )

    object ArticleDetail : Screen(
        route = "article_detail",
        title = "Detalhes do artigo",
        isBaseScreen = false
    )

    object Counter : Screen(
        route = "counter",
        title = "Contador",
        isBaseScreen = false
    )

    object Cart : Screen(
        route = "cart",
        title = "Carrinho",
        isBaseScreen = true
    )
}
