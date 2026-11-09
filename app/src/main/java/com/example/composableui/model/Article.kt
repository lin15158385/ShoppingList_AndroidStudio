package com.example.composableui.model

import org.json.JSONObject

data class Article(
    val id: Int,
    val title: String,
    val type: String,
    val content: String,
    val imageUrl: String? = null
){
    companion object {
        fun fromJson(product: JSONObject, index: Int): Article {
            val title = product.optString("product_name")

            val imageUrl = product.optString("image_url").takeIf {
                it.isNotBlank() && it != "null" && it.startsWith("http")
            }

            return Article(
                id = product.optString("code").toIntOrNull() ?: (index + 1),
                title = title,
                type = product.optString("categories")
                    .ifBlank { "Categoria desconhecida" },
                content = product.optString("ingredients_text")
                    .ifBlank { "Ingredientes não disponíveis" },
                imageUrl = imageUrl
            )
        }
    }
}