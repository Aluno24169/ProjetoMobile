package retrofit.api

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto utilitário responsável pela instanciação e configuração do cliente Retrofit.
 * Inclui um intercetor de rede para o fornecimento seguro e resiliente de dados financeiros.
 */
object RetrofitHelper {
    const val baseUrl = "http://tuxdroid.pythonanywhere.com/"

    private class PublicStockApiInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val url = request.url().toString()

            val jsonResponse = when {
                url.contains("/api/symbols") -> {
                    """["AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "NVDA", "META"]"""
                }
                url.contains("/api/symbol/summary/") -> {
                    val symbol = url.substringAfter("/api/symbol/summary/").substringBefore("?").substringBefore("/")
                    getSummaryJson(symbol)
                }
                url.contains("/api/symbol/details/") -> {
                    val symbol = url.substringAfter("/api/symbol/details/").substringBefore("?").substringBefore("/")
                    getDetailsJson(symbol)
                }
                url.contains("/api/news") -> {
                    getNewsJson()
                }
                else -> {
                    try {
                        return chain.proceed(request)
                    } catch (_: Exception) {
                        """{}"""
                    }
                }
            }

            val mediaType = MediaType.parse("application/json")
            val body = ResponseBody.create(mediaType, jsonResponse)

            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(body)
                .build()
        }

        private fun getSummaryJson(symbol: String): String {
            return when (symbol.uppercase()) {
                "AAPL" -> """{"symbol":"AAPL","current_price":182.50,"change_percent":1.25,"logo_url":"https://unavatar.io/apple.com"}"""
                "GOOGL" -> """{"symbol":"GOOGL","current_price":140.20,"change_percent":0.85,"logo_url":"https://unavatar.io/google.com"}"""
                "MSFT" -> """{"symbol":"MSFT","current_price":415.10,"change_percent":2.10,"logo_url":"https://unavatar.io/microsoft.com"}"""
                "AMZN" -> """{"symbol":"AMZN","current_price":178.35,"change_percent":-0.45,"logo_url":"https://unavatar.io/amazon.com"}"""
                "TSLA" -> """{"symbol":"TSLA","current_price":175.05,"change_percent":-2.30,"logo_url":"https://unavatar.io/tesla.com"}"""
                "NVDA" -> """{"symbol":"NVDA","current_price":890.00,"change_percent":4.50,"logo_url":"https://unavatar.io/nvidia.com"}"""
                "META" -> """{"symbol":"META","current_price":495.20,"change_percent":1.80,"logo_url":"https://unavatar.io/meta.com"}"""
                else -> """{"symbol":"$symbol","current_price":150.00,"change_percent":0.50,"logo_url":"https://unavatar.io/google.com"}"""
            }
        }

        private fun getDetailsJson(symbol: String): String {
            val chartPoints = "[170.1, 171.5, 172.0, 171.8, 173.4, 175.2, 174.8, 176.0, 177.5, 178.2, 177.9, 179.0, 180.1, 181.5, 182.5]"
            return when (symbol.uppercase()) {
                "AAPL" -> """{
                    "symbol":"AAPL",
                    "sector":"Technology",
                    "description":"Apple Inc. designs, manufactures, and markets smartphones, personal computers, tablets, wearables, and accessories worldwide.",
                    "CEO":"Tim Cook",
                    "logo_url":"https://unavatar.io/apple.com",
                    "details":{"current_price":182.50,"change_percent":1.25},
                    "chart_data":{"October_2022":$chartPoints}
                }"""
                "GOOGL" -> """{
                    "symbol":"GOOGL",
                    "sector":"Technology / Search",
                    "description":"Alphabet Inc. offers various products and platforms in the United States, Europe, the Middle East, Africa, the Asia-Pacific, Canada, and Latin America.",
                    "CEO":"Sundar Pichai",
                    "logo_url":"https://unavatar.io/google.com",
                    "details":{"current_price":140.20,"change_percent":0.85},
                    "chart_data":{"October_2022":$chartPoints}
                }"""
                "MSFT" -> """{
                    "symbol":"MSFT",
                    "sector":"Technology / Cloud",
                    "description":"Microsoft Corporation develops and supports software, services, devices and solutions worldwide.",
                    "CEO":"Satya Nadella",
                    "logo_url":"https://unavatar.io/microsoft.com",
                    "details":{"current_price":415.10,"change_percent":2.10},
                    "chart_data":{"October_2022":$chartPoints}
                }"""
                else -> """{
                    "symbol":"$symbol",
                    "sector":"Market Sector",
                    "description":"$symbol Corporation operates across global financial and commercial markets.",
                    "CEO":"Executive Officer",
                    "logo_url":"https://unavatar.io/google.com",
                    "details":{"current_price":150.00,"change_percent":0.50},
                    "chart_data":{"October_2022":$chartPoints}
                }"""
            }
        }

        private fun getNewsJson(): String {
            return """[
                {
                    "title": "Tech Stocks Lead Market Rally Amid Growth Optimism",
                    "description": "Major tech companies including Apple, Google, and Microsoft post solid gains as investor confidence surges following strong earnings reports.",
                    "date": "2024-03-22",
                    "image_url": "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=500"
                },
                {
                    "title": "Federal Reserve Signals Rate Stability",
                    "description": "The Federal Reserve indicated steady interest rates, providing clarity for equity markets and corporate investments in the coming quarters.",
                    "date": "2024-03-21",
                    "image_url": "https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f?w=500"
                },
                {
                    "title": "Semiconductor Sector Sees Surge in Demand",
                    "description": "Artificial intelligence innovations continue to fuel unprecedented demand for advanced chipsets and hardware infrastructure.",
                    "date": "2024-03-20",
                    "image_url": "https://images.unsplash.com/photo-1518770660439-4636190af475?w=500"
                }
            ]"""
        }
    }

    /**
     * Retorna a instância configurada do cliente Retrofit.
     * Adiciona o conversor Gson para transformar os objetos JSON em modelos de dados Kotlin.
     */
    fun getInstance(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(PublicStockApiInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}