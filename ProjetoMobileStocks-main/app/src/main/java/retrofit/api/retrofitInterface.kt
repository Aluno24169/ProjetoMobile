package retrofit.api

import retrofit.models.News
import retrofit.models.SymbolDetails
import retrofit.models.SymbolSummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface Retrofit que define os contratos de comunicação HTTP com o serviço de dados financeiros.
 */
interface retrofitInterface {

    /**
     * Obtém a lista com os símbolos de todas as ações disponíveis no mercado.
     */
    @GET("/api/symbols")
    suspend fun getSymbols(): Response<List<String>>

    /**
     * Obtém o resumo de cotação para o símbolo financeiro especificado.
     *
     * @param symbol Código do símbolo da ação (ex: "AAPL").
     */
    @GET("/api/symbol/summary/{symbol}")
    suspend fun getSymbolSummary(@Path("symbol") symbol: String): Response<SymbolSummary>

    /**
     * Obtém as informações detalhadas e o histórico do gráfico para o símbolo especificado.
     *
     * @param symbol Código do símbolo da ação (ex: "AAPL").
     */
    @GET("/api/symbol/details/{symbol}")
    suspend fun getSymbolDetails(@Path("symbol") symbol: String): Response<SymbolDetails>

    /**
     * Obtém a lista de notícias mais recentes do mercado financeiro.
     */
    @GET("/api/news")
    suspend fun getNews(): Response<List<News>>
}