package com.example.ProjetoMobileStocks

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit.models.News
import retrofit.api.RetrofitHelper
import retrofit.models.SymbolDetails
import retrofit.models.SymbolSummary
import retrofit.api.retrofitInterface

/**
 * Conjunto de testes unitários para verificação do comportamento e integração do repositório de dados.
 */
class BackendDataTest {

    private lateinit var symbolsAPI: retrofitInterface
    private lateinit var backend: BackendData

    /**
     * Configuração inicial executada antes de cada teste unitário.
     */
    @Before
    fun setUp() {
        symbolsAPI = RetrofitHelper.getInstance().create(retrofitInterface::class.java)
        backend = BackendData()
    }

    /**
     * Testa se a obtenção da lista de símbolos de ações retorna os símbolos principais esperados.
     */
    @Test
    fun testGetSymbols() = runBlocking {
        val symbolsResponse = backend.getSymbolsList()
        verifySymbolsListIntegrity(symbolsResponse)
    }

    /**
     * Testa se o pedido de notícias do mercado financeiro retorna uma lista válida de objetos [News].
     */
    @Test
    fun testGetNews() = runBlocking {
        val response = symbolsAPI.getNews()
        assertTrue(response.isSuccessful)
        assertTrue(response.body() is List<News>)
    }

    /**
     * Testa se os detalhes de cada símbolo são obtidos com sucesso.
     */
    @Test
    fun testGetSymbolDetails() = runBlocking {
        val symbolsList = backend.getSymbolsList()
        verifySymbolsListIntegrity(symbolsList)

        val responseList = symbolsList.map { symbolsAPI.getSymbolDetails(it) }
        responseList.forEach { response ->
            assertTrue(response.isSuccessful)
            assertTrue(response.body() is SymbolDetails)
        }
    }

    /**
     * Testa se os resumos de cotação de cada símbolo são obtidos com sucesso.
     */
    @Test
    fun testGetSymbolSummary() = runBlocking {
        val symbolsList = backend.getSymbolsList()
        verifySymbolsListIntegrity(symbolsList)

        val responseList = symbolsList.map { symbolsAPI.getSymbolSummary(it) }
        responseList.forEach { response ->
            assertTrue(response.isSuccessful)
            assertTrue(response.body() is SymbolSummary)
        }
    }

    /**
     * Valida a integridade e presença dos símbolos obrigatórios na lista.
     */
    private fun verifySymbolsListIntegrity(symbols: List<String>) {
        assertTrue(symbols.isNotEmpty())
        assertTrue(symbols.contains("AAPL"))
        assertTrue(symbols.contains("GOOGL"))
        assertTrue(symbols.contains("MSFT"))
        symbols.forEach { assertNotNull(it) }
    }
}
