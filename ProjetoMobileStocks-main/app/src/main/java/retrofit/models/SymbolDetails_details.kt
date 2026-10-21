package retrofit.models

/**
 * Modelo de dados com o resumo de preço e variação contido nos detalhes do símbolo.
 */
data class SymbolDetails_details(
    val change_percent: Double,
    val current_price: Double
)