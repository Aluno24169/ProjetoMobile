package com.example.ProjetoMobileStocks.ui.home.stockFragments.checklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ProjetoMobileStocks.BackendData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit.models.SymbolSummary

/**
 * ViewModel responsável pela gestão do estado e carregamento assíncrono da lista de resumos das ações.
 */
class ChecklistViewModel : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val data = BackendData()

    private val _symbolSummaryList = MutableLiveData<List<SymbolSummary>?>()
    val symbolSummaryList: MutableLiveData<List<SymbolSummary>?> get() = _symbolSummaryList

    private var fetchedData: List<SymbolSummary>? = null

    init {
        if (fetchedData == null) {
            refreshData()
        }
    }

    /**
     * Efetua o pedido de atualização dos dados das ações através do repositório em segundo plano.
     */
    fun refreshData(@Suppress("UNUSED_PARAMETER") isRefreshed: Boolean = false) {
        coroutineScope.launch {
            fetchedData = data.fetchSymbolSummary()
            _symbolSummaryList.postValue(fetchedData)
        }
    }
}
