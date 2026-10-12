
package com.example.composableui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composableui.model.Article
import com.example.composableui.model.ManagedArticleList

@Composable
fun ListEditorScreen(
    articleList: ManagedArticleList,
    onBack: () -> Unit,
    onAddArticle: (Article) -> Unit,
    onUpdateArticle: (Article) -> Unit,
    onDeleteArticle: (Article) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingArticle by remember { mutableStateOf<Article?>(null) }

    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var articleToDelete by remember {
        mutableStateOf<Article?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("A editar: ${articleList.name}")

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                editingArticle = null
                title = ""
                type = ""
                content = ""
                showDialog = true
            }
        ) {
            Text("Adicionar artigo")
        }

        if (articleList.articles.isEmpty()) {
            Text("Esta lista ainda não tem artigos.")
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = articleList.articles,
                key = { it.id }
            ) { article ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(article.title)
                        Text("Tipo: ${article.type}")
                        Text(article.content)

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    editingArticle = article
                                    title = article.title
                                    type = article.type
                                    content = article.content
                                    showDialog = true
                                }
                            ) {
                                Text("Modificar")
                            }

                            Button(
                                onClick = {
                                    articleToDelete = article
                                }
                            ) {
                                Text("Eliminar artigo")
                            }
                        }
                    }
                }
            }
        }

        Button(onClick = onBack) {
            Text("Voltar às listas")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text(
                    if (editingArticle == null)
                        "Novo artigo"
                    else
                        "Modificar artigo"
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título") }
                    )

                    OutlinedTextField(
                        value = type,
                        onValueChange = { type = it },
                        label = { Text("Tipo") }
                    )

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Conteúdo") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = title.isNotBlank(),
                    onClick = {
                        val article = Article(
                            id = editingArticle?.id
                                ?: ((articleList.articles
                                    .maxOfOrNull { it.id } ?: 0) + 1),
                            title = title.trim(),
                            type = type.trim(),
                            content = content.trim()
                        )

                        if (editingArticle == null) {
                            onAddArticle(article)
                        } else {
                            onUpdateArticle(article)
                        }

                        showDialog = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (articleToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                articleToDelete = null
            },
            title = {
                Text("Eliminar artigo")
            },
            text = {
                Text(
                    "Tens a certeza de que queres eliminar " +
                            "\"${articleToDelete?.title}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        articleToDelete?.let { article ->
                            onDeleteArticle(article)
                        }
                        articleToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        articleToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
