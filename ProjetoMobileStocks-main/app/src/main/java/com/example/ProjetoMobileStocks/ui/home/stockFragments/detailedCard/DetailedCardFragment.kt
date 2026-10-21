package com.example.ProjetoMobileStocks.ui.home.stockFragments.detailedCard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.example.ProjetoMobileStocks.R
import com.example.ProjetoMobileStocks.ui.home.stockFragments.simpleCard.SimpleCardFragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.squareup.picasso.Picasso
import retrofit.models.SymbolDetails
import retrofit.models.SymbolSummary

/**
 * Fragmento responsável pela apresentação detalhada do perfil corporativo e do gráfico de cotações da ação selecionada.
 */
class DetailedCardFragment(chosenSymbol: SymbolSummary) : Fragment() {
    lateinit var progressBar: ProgressBar
    lateinit var noDataFoundtv: TextView
    private val chosenSymbol = chosenSymbol
    private var symbolDetailedList: MutableList<SymbolDetails> = mutableListOf()
    lateinit var lineGraphView: GraphView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detailed_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("DetailedCardFragment", "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this).get(DetailedCardViewModel::class.java)

        noDataFoundtv = view.findViewById(R.id.tvDetailedNotFound)
        noDataFoundtv.visibility = View.GONE

        progressBar = view.findViewById(R.id.progressBarDetailed)
        showProgressBar()

        viewModel.symbolDetailsList.observe(viewLifecycleOwner) { symbolDetails ->
            symbolDetailedList.clear()
            symbolDetailedList.addAll(symbolDetails)
            val symbolItem = symbolDetailedList.find { it.symbol == chosenSymbol.symbol }
            Log.d("DetailedCardFragment", "Detalhes carregados: $symbolItem")

            val price: TextView = view.findViewById(R.id.tvPriceDetailed)
            val symbol: TextView = view.findViewById(R.id.tvSymbolDetailed)
            val percentage: TextView = view.findViewById(R.id.tvPercentDetailed)
            val sector: TextView = view.findViewById(R.id.tvSectorDetailed)
            val imageView: ImageView = view.findViewById(R.id.imageviewDetailed)
            lineGraphView = view.findViewById(R.id.idGraphView)
            lineGraphView.visibility = View.GONE

            if (symbolItem != null) {
                populateGraph(symbolItem, lineGraphView)
                sector.text = symbolItem.sector
                price.text = symbolItem.details.current_price.toString()
                symbol.text = symbolItem.symbol
                percentage.text = symbolItem.details.change_percent.toString()
                
                if (symbolItem.logo_url.isNotEmpty()) {
                    Picasso.get()
                        .load(symbolItem.logo_url)
                        .placeholder(R.drawable.ic_home_black_24dp)
                        .error(R.drawable.ic_home_black_24dp)
                        .fit()
                        .centerInside()
                        .into(imageView)
                } else {
                    imageView.setImageResource(R.drawable.ic_home_black_24dp)
                }

                val color = if (symbolItem.details.change_percent < 0) R.color.red else R.color.green
                percentage.setTextColor(ContextCompat.getColor(requireContext(), color))
            } else {
                noDataFoundtv.visibility = View.VISIBLE
            }
            hideProgressBar()
        }

        val button: Button = view.findViewById(R.id.btnHome)
        button.text = getString(R.string.home)
        button.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.display_fragment, SimpleCardFragment(mutableListOf(), false))
                .addToBackStack(null)
                .commit()
        }
    }

    /**
     * Preenche e configura a série do gráfico de linhas com os pontos históricos de cotação.
     */
    fun populateGraph(symbolDetails: SymbolDetails, graph: GraphView) {
        val graphList = mutableListOf<DataPoint>()

        for ((index, value) in symbolDetails.chart_data.October_2022.withIndex()) {
            graphList.add(DataPoint(index.toDouble(), value))
        }

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(graphList.toTypedArray())

        graph.viewport.setXAxisBoundsManual(true)
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(graphList.size.toDouble() - 1)

        graph.viewport.isScrollable = true
        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)
        graph.viewport.setScrollableY(true)

        series.color = ContextCompat.getColor(requireContext(), R.color.accent_cyan)
        series.thickness = 8

        graph.gridLabelRenderer.gridColor = ContextCompat.getColor(requireContext(), R.color.bg_input_dark)
        graph.gridLabelRenderer.horizontalLabelsColor = ContextCompat.getColor(requireContext(), R.color.text_secondary)
        graph.gridLabelRenderer.verticalLabelsColor = ContextCompat.getColor(requireContext(), R.color.text_secondary)

        graph.addSeries(series)
        graph.visibility = View.VISIBLE
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}
