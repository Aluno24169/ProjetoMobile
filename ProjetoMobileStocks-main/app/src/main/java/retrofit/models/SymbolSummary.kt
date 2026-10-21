package retrofit.models

/**
 * Modelo de dados que representa o resumo de cotação de um determinado símbolo.
 */
data class SymbolSummary(
    val change_percent: Double,
    val current_price: Double,
    val logo_url: String,
    val symbol: String
)