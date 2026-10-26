package com.example.composableui.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composableui.data.sampleArticles
import com.example.composableui.model.Article

@Composable
fun DetailScreen(
    article: Article,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(article.title)

        Text(article.type)

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(article.content)

        Button(
            onClick = onBack
        ) {
            Text("Voltar")
        }
    }
}

@Preview(showBackground = true, name = "Detalhes do artigo")
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        article = sampleArticles.first(),
        onBack = {}
    )
}
