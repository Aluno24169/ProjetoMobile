package retrofit.models

/**
 * Modelo de dados que representa uma notícia do mercado financeiro.
 */
data class News(
    val date: String,
    val description: String,
    val image_url: String,
    val title: String
)