package com.example.ProjetoMobileStocks

import retrofit.models.News
import retrofit.models.SymbolDetails
import retrofit.models.SymbolSummary
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import retrofit.api.RetrofitHelper
import retrofit.api.retrofitInterface
import kotlinx.coroutines.Dispatchers
import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * Repositório central de dados responsável pela coordenação dos pedidos à API remota
 * e pela gestão do estado assíncrono das operações de rede.
 */
class BackendData {
    private val symbolsAPI = RetrofitHelper.getInstance().create(retrofitInterface::class.java)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    var symbolsListDeferred: Deferred<List<String>>? = null

    init {
        getSymbols()
    }

    /**
     * Inicia o pedido assíncrono para obter a lista com todos os símbolos disponíveis.
     */
    fun getSymbols() {
        try {
            symbolsListDeferred = coroutineScope.async {
                val symbolsResponse = symbolsAPI.getSymbols()
                if (symbolsResponse.isSuccessful) {
                    symbolsResponse.body() ?: emptyList()
                } else {
                    emptyList()
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Aguarda a conclusão do pedido assíncrono e retorna a lista de símbolos de ações.
     */
    suspend fun getSymbolsList(): List<String> {
        return symbolsListDeferred?.await() ?: emptyList()
    }


    /**
     * Obtém assincronamente as notícias mais recentes do mercado financeiro.
     */
    suspend fun fetchNews(): List<News> {
        try {
            val newsResponse: Response<List<News>> = symbolsAPI.getNews()
            val news: List<News>?

            if (newsResponse.isSuccessful) {
                news = newsResponse.body()
            } else {
                Log.e("BackendData", "Falha ao obter as notícias do mercado")
                return emptyList()
            }
            if (news.isNullOrEmpty()) {
                return emptyList()
            }
            return news
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Obtém os detalhes completos e métricas corporativas de cada um dos símbolos ativos.
     */
    suspend fun fetchSymbolDetailsList(): List<SymbolDetails> {
        try {
            val symbolsList = getSymbolsList()
            if (symbolsList.isEmpty()) {
                Log.e("BackendData", "Nenhum símbolo disponível para obter detalhes")
                return emptyList()
            }

            val symbolDetailsList = mutableListOf<SymbolDetails>()

            for (symbol in symbolsList) {
                val responseSymbolDetail: Response<SymbolDetails> = symbolsAPI.getSymbolDetails(symbol)
                if (responseSymbolDetail.isSuccessful) {
                    val symbolDetails = responseSymbolDetail.body()
                    if (symbolDetails != null) {
                        symbolDetailsList.add(symbolDetails)
                    }
                } else {
                    Log.e("BackendData", "Falha ao obter detalhes para a ação: $symbol")
                }
            }
            return symbolDetailsList
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Obtém o resumo de cotações (preço atual e percentagem de variação) para a lista de símbolos.
     */
    suspend fun fetchSymbolSummary(): List<SymbolSummary> {
        try {
            val symbolSummaryList = mutableListOf<SymbolSummary>()
            val symbolsList = getSymbolsList()

            if (symbolsList.isEmpty()) {
                Log.e("BackendData", "Nenhum símbolo disponível para obter resumo")
                return emptyList()
            }
            for (symbol in symbolsList) {
                val responseSymbolSummary: Response<SymbolSummary> =
                    symbolsAPI.getSymbolSummary(symbol)
                if (responseSymbolSummary.isSuccessful) {
                    val symbolSummary = responseSymbolSummary.body()
                    if (symbolSummary != null) {
                        symbolSummaryList.add(symbolSummary)
                    }
                } else {
                    Log.e("BackendData", "Falha ao obter resumo para a ação: $symbol")
                }
            }
            Log.d("BackendData", "Resumos obtidos com sucesso: ${symbolSummaryList.size} itens")
            return symbolSummaryList
        } catch (e: Exception) {
            throw e
        }
    }

}
