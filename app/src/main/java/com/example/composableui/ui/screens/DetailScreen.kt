
package com.example.composableui.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composableui.R
import com.example.composableui.data.sampleArticles
import com.example.composableui.model.Article

@Composable
fun DetailScreen(
    article: Article
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.default_product)
                .error(R.drawable.default_product)
                .build(),
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(article.title)

        Text(article.type)

        Spacer(modifier = Modifier.height(16.dp))

        Text(article.content)
    }
}

@Preview(showBackground = true, name = "Detalhes do artigo")
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        article = sampleArticles.first()
    )
}
