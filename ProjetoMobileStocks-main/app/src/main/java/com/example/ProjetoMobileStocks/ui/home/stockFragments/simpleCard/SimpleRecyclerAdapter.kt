package com.example.ProjetoMobileStocks.ui.home.stockFragments.simpleCard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ProjetoMobileStocks.R
import com.squareup.picasso.Picasso
import retrofit.models.SymbolSummary

/**
 * Adaptador de RecyclerView para a apresentação dos cartões das ações presentes na carteira do utilizador.
 */
class SimpleRecyclerAdapter(
    private val context: Context,
    private var stockList: List<SymbolSummary>
) : RecyclerView.Adapter<SimpleRecyclerAdapter.MyViewHolder>() {

    private var clickListener: DetailedViewOnClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_simple_card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = stockList[position]
        holder.price.text = currentItem.current_price.toString()
        holder.symbol.text = currentItem.symbol
        holder.percentage.text = currentItem.change_percent.toString()

        val color = if (currentItem.change_percent < 0) {
            R.color.red
        } else {
            R.color.green
        }

        holder.percentage.setTextColor(ContextCompat.getColor(context, color))
        holder.percgraph.setTextColor(ContextCompat.getColor(context, color))

        // Roda o indicador de seta em 180 graus caso a variação percentual seja negativa
        val rotationAngle = if (currentItem.change_percent < 0) {
            180f
        } else {
            0f
        }
        holder.percgraph.rotation = rotationAngle

        // Carrega o logótipo corporativo da ação de forma assíncrona com tratamento de erros
        if (currentItem.logo_url.isNotEmpty()) {
            Picasso.get()
                .load(currentItem.logo_url)
                .placeholder(R.drawable.ic_home_black_24dp)
                .error(R.drawable.ic_home_black_24dp)
                .fit()
                .centerInside()
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.ic_home_black_24dp)
        }

        holder.itemView.setOnClickListener {
            Log.d("SimpleRecyclerAdapter", "Item selecionado: ${currentItem.symbol}")
            clickListener?.onDetailedViewClick(currentItem)
        }
    }

    /**
     * ViewHolder que mapeia os elementos visuais de cada cartão de ação.
     */
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val symbol: TextView = itemView.findViewById(R.id.tvSymbol)
        val percentage: TextView = itemView.findViewById(R.id.tvPercentage)
        val percgraph: TextView = itemView.findViewById(R.id.tvPercentageGraph)
    }

    fun setDetailedItemClickListener(listener: DetailedViewOnClick) {
        clickListener = listener
    }

    /**
     * Interface para notificação do evento de clique para visualização detalhada.
     */
    interface DetailedViewOnClick {
        fun onDetailedViewClick(symbolSummary: SymbolSummary)
    }
}
