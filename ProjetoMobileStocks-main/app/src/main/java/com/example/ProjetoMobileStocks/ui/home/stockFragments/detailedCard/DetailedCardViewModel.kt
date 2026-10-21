package com.example.ProjetoMobileStocks.ui.home.stockFragments.detailedCard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ProjetoMobileStocks.BackendData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit.models.SymbolDetails

/**
 * ViewModel responsável pelo fornecimento e gestão dos detalhes completos das ações e dos seus gráficos.
 */
class DetailedCardViewModel : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val data = BackendData()

    private val _symbolDetailsList = MutableLiveData<List<SymbolDetails>>()
    val symbolDetailsList: LiveData<List<SymbolDetails>> get() = _symbolDetailsList

    init {
        coroutineScope.launch {
            val symbolDetailsList = data.fetchSymbolDetailsList()
            _symbolDetailsList.postValue(symbolDetailsList)
        }
    }
}
