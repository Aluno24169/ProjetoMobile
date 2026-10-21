package com.example.ProjetoMobileStocks.ui.News

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ProjetoMobileStocks.BackendData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit.models.News

/**
 * ViewModel responsável pelo carregamento e disponibilização da lista de notícias financeiras.
 */
class NewsViewModel : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val data = BackendData()

    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> get() = _newsList

    init {
        coroutineScope.launch {
            val newsList = data.fetchNews()
            _newsList.postValue(newsList)
        }
    }
}
