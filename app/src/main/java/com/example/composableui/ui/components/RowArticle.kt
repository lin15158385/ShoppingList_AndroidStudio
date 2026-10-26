
package com.example.composableui.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composableui.data.sampleArticles
import com.example.composableui.model.Article

@Composable
fun RowArticle(
    article: Article,
    onPreview: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(article.title)
            Text(article.type)

            Button(
                onClick = onPreview,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Ver detalhes")
            }
        }
    }
}

@Preview(showBackground = true, name = "Linha de artigo")
@Composable
fun RowArticlePreview() {
    RowArticle(
        article = sampleArticles.first(),
        onPreview = {}
    )
}
