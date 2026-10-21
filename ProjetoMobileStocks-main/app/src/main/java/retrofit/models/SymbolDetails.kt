package retrofit.models

/**
 * Modelo de dados que representa as informações detalhadas e o gráfico de um determinado símbolo.
 */
data class SymbolDetails(
    val CEO: String,
    val chart_data: ChartData,
    val description: String,
    val details: SymbolDetails_details,
    val logo_url: String,
    val sector: String,
    val symbol: String
)