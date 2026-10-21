package com.example.ProjetoMobileStocks.ui.home.stockFragments.simpleCard

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ProjetoMobileStocks.R
import com.example.ProjetoMobileStocks.ui.home.stockFragments.checklist.ChecklistFragment
import com.example.ProjetoMobileStocks.ui.home.stockFragments.detailedCard.DetailedCardFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit.models.SymbolSummary

/**
 * Fragmento responsável pela apresentação dos cartões das ações presentes na carteira do utilizador.
 * Permite também aceder à gestão da lista e navegar para a vista detalhada de cada título.
 */
class SimpleCardFragment(
    val watchList: MutableList<SymbolSummary>,
    val isFromWatchlist: Boolean
) : Fragment(), SimpleRecyclerAdapter.DetailedViewOnClick {

    private lateinit var noDataTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SimpleCardFragment", "onViewCreated()")
        noDataTV = requireView().findViewById(R.id.tvSimpleNoData)
        noDataTV.visibility = View.GONE

        if (watchList.isEmpty() && !isFromWatchlist) {
            retrieveData()
        }

        val button: Button = view.findViewById(R.id.btnChecklist)
        button.text = getString(R.string.edit_watchlist)
        button.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.display_fragment, ChecklistFragment(watchList))
                .addToBackStack("SimpleFragment")
                .commit()
        }
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    override fun onStart() {
        super.onStart()
        if (watchList.isEmpty()) {
            noDataTV.visibility = View.VISIBLE
        }
        val itemAdapter = SimpleRecyclerAdapter(requireContext(), watchList)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycleView)
        itemAdapter.setDetailedItemClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
    }

    /**
     * Guarda a lista atual de ações nas SharedPreferences em formato JSON.
     */
    private fun saveData() {
        val sharedPreferences = requireContext().getSharedPreferences("editList", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(watchList)

        editor.putString("symbolList", json)
        editor.apply()
    }

    /**
     * Recupera a lista guardada de ações a partir das SharedPreferences.
     */
    private fun retrieveData() {
        val sharedPreferences = requireContext().getSharedPreferences("editList", Context.MODE_PRIVATE)
        var json = sharedPreferences.getString("symbolList", "")
        if (json.isNullOrEmpty()) {
            json = sharedPreferences.getString("tickerList", "")
        }

        val gson = Gson()
        val type = object : TypeToken<List<SymbolSummary>>() {}.type

        val savedList: List<SymbolSummary> = gson.fromJson(json, type) ?: emptyList()
        if (savedList.isEmpty()) {
            noDataTV.visibility = View.VISIBLE
        }
        watchList.clear()
        watchList.addAll(savedList)
    }

    /**
     * Trata o clique num item da lista para transição para o ecrã de detalhes da ação.
     */
    override fun onDetailedViewClick(symbolSummary: SymbolSummary) {
        Log.d("SimpleCardFragment", "Ação selecionada para detalhes: ${symbolSummary.symbol}")

        parentFragmentManager.beginTransaction()
            .replace(R.id.display_fragment, DetailedCardFragment(symbolSummary))
            .addToBackStack(null)
            .commit()
    }
}
