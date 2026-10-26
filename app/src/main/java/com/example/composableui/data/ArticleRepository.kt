
package com.example.composableui.data

import com.example.composableui.model.Article
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ArticleRepository {

    private val client = OkHttpClient()

    private val apiUrl =
        "https://world.openfoodfacts.org/cgi/search.pl?action=process&json=true&page_size=20&fields=product_name,categories,ingredients_text,code"

    fun fetchProducts(callback: (Result<List<Article>>) -> Unit) {
        val request = Request.Builder()
            .url(apiUrl)
            .header("User-Agent", "ComposableUI/1.0")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                callback(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    try {
                        if (!it.isSuccessful) {
                            throw IOException("Erro HTTP: ${it.code}")
                        }

                        val body = it.body?.string()
                            ?: throw IOException("Resposta vazia")

                        val json = JSONObject(body)
                        val products = json.optJSONArray("products")
                            ?: throw IOException("Não foram encontrados produtos")

                        val articles = mutableListOf<Article>()

                        for (i in 0 until products.length()) {
                            val product = products.optJSONObject(i) ?: continue

                            val title = product.optString("product_name")
                            if (title.isBlank() || title == "null") continue

                            articles.add(
                                Article(
                                    id = product.optString("code")
                                        .toIntOrNull() ?: (i + 1),
                                    title = title,
                                    type = product.optString("categories")
                                        .ifBlank { "Categoria desconhecida" },
                                    content = product.optString("ingredients_text")
                                        .ifBlank { "Ingredientes não disponíveis" }
                                )
                            )
                        }

                        callback(Result.success(articles))
                    } catch (e: Exception) {
                        callback(Result.failure(e))
                    }
                }
            }
        })
    }
}
