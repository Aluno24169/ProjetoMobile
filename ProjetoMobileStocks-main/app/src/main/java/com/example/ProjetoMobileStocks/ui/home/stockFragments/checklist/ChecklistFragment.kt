package com.example.ProjetoMobileStocks.ui.home.stockFragments.checklist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ProjetoMobileStocks.R
import com.example.ProjetoMobileStocks.ui.home.stockFragments.simpleCard.SimpleCardFragment
import retrofit.models.SymbolSummary

/**
 * Fragmento que disponibiliza a lista de seleção e pesquisa para adição e remoção de ações na carteira.
 */
class ChecklistFragment(val watchList: MutableList<SymbolSummary>) :
    Fragment(), ChecklistRecyclerAdapter.ChecklistItemClickListener {

    private lateinit var itemAdapter: ChecklistRecyclerAdapter
    private lateinit var noDataTV: TextView
    val symbolsSummaryList = mutableListOf<SymbolSummary>()
    lateinit var progressBar: ProgressBar
    val viewModel: ChecklistViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checklist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("ChecklistFragment", "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        noDataTV = requireView().findViewById(R.id.tvCheckListNoData)
        noDataTV.visibility = View.GONE

        val button: Button = view.findViewById(R.id.btnSimple)
        val buttonRefresh: Button = view.findViewById(R.id.buttonRefresh)
        button.text = getString(R.string.home)
        button.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.display_fragment, SimpleCardFragment(watchList, true))
                .addToBackStack(null)
                .commit()
        }

        progressBar = view.findViewById(R.id.progressBar)
        showProgressBar()

        viewModel.symbolSummaryList.observe(viewLifecycleOwner) { summaryListFromVM ->
            Log.d("ChecklistFragment", "Resumos recebidos no LiveData: $summaryListFromVM")
            symbolsSummaryList.clear()
            if (summaryListFromVM != null) {
                symbolsSummaryList.addAll(summaryListFromVM)
            }

            itemAdapter = ChecklistRecyclerAdapter(requireContext(), symbolsSummaryList, watchList)
            val recyclerView: RecyclerView = view.findViewById(R.id.recycleView)
            itemAdapter.setChecklistItemClickListener(this)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = itemAdapter
            hideProgressBar()
            noDataTV.visibility = if (symbolsSummaryList.isEmpty()) View.VISIBLE else View.GONE

            view.findViewById<EditText>(R.id.etSearch).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    filter(p0.toString())
                }
            })
        }

        buttonRefresh.setOnClickListener {
            showProgressBar()
            viewModel.refreshData(true)
        }
    }

    /**
     * Filtra a lista de ações visíveis pelo prefixo do símbolo introduzido.
     */
    fun filter(text: String) {
        if (text.isNotEmpty()) {
            val filteredSymbols = symbolsSummaryList.filter { it.symbol.startsWith(text, ignoreCase = true) }
            itemAdapter.filter(filteredSymbols)
        } else {
            itemAdapter.filter(symbolsSummaryList)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData(true)
    }

    override fun onAddButtonClick(symbolSummary: SymbolSummary) {
        watchList.add(symbolSummary)
    }

    override fun onRemoveButtonClick(symbolSummary: SymbolSummary) {
        watchList.remove(symbolSummary)
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}
