package com.example.ProjetoMobileStocks.ui.home.stockFragments.checklist

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ProjetoMobileStocks.R
import com.squareup.picasso.Picasso
import retrofit.models.SymbolSummary

/**
 * Adaptador de RecyclerView para a apresentação dos cartões na lista de seleção com botões de adicionar e remover.
 */
class ChecklistRecyclerAdapter(
    private val context: Context,
    private var stockList: List<SymbolSummary>,
    private var addedItems: List<SymbolSummary>
) : RecyclerView.Adapter<ChecklistRecyclerAdapter.MyViewHolder>() {

    private var clickListener: ChecklistItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_checklist_card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemsViewModel = stockList[position]
        holder.price.text = itemsViewModel.current_price.toString()
        holder.symbol.text = itemsViewModel.symbol
        holder.percentage.text = itemsViewModel.change_percent.toString()

        if (itemsViewModel.logo_url.isNotEmpty()) {
            Picasso.get()
                .load(itemsViewModel.logo_url)
                .placeholder(R.drawable.ic_home_black_24dp)
                .error(R.drawable.ic_home_black_24dp)
                .fit()
                .centerInside()
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.ic_home_black_24dp)
        }

        val color = if (itemsViewModel.change_percent < 0) R.color.red else R.color.green
        holder.percentage.setTextColor(ContextCompat.getColor(context, color))

        val isInWatchList = addedItems.any { it.symbol == itemsViewModel.symbol }
        val removeText = context.getString(R.string.remove)
        val addText = context.getString(R.string.add)

        if (isInWatchList) {
            holder.addButton.text = removeText
            holder.addButton.setBackgroundResource(R.drawable.btn_futuristic_secondary)
            holder.addButton.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else {
            holder.addButton.text = addText
            holder.addButton.setBackgroundResource(R.drawable.btn_futuristic_primary)
            holder.addButton.setTextColor(ContextCompat.getColor(context, R.color.bg_dark_blue))
        }

        holder.addButton.setOnClickListener {
            if (holder.addButton.text == removeText) {
                clickListener?.onRemoveButtonClick(itemsViewModel)
                holder.addButton.text = addText
                holder.addButton.setBackgroundResource(R.drawable.btn_futuristic_primary)
                holder.addButton.setTextColor(ContextCompat.getColor(context, R.color.bg_dark_blue))
            } else {
                clickListener?.onAddButtonClick(itemsViewModel)
                holder.addButton.text = removeText
                holder.addButton.setBackgroundResource(R.drawable.btn_futuristic_secondary)
                holder.addButton.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }

    /**
     * ViewHolder que mapeia os elementos visuais de cada cartão da lista de seleção.
     */
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val price: TextView = itemView.findViewById(R.id.tvPriceChecklist)
        val symbol: TextView = itemView.findViewById(R.id.tvSymbolChecklist)
        val percentage: TextView = itemView.findViewById(R.id.tvPercentageChecklist)
        val addButton: Button = itemView.findViewById(R.id.btnAddChecklist)
        val imageView: ImageView = itemView.findViewById(R.id.imgViewLogoChecklist)
    }

    fun setChecklistItemClickListener(listener: ChecklistItemClickListener) {
        clickListener = listener
    }

    /**
     * Interface para escuta dos eventos de adição e remoção de ações na carteira.
     */
    interface ChecklistItemClickListener {
        fun onAddButtonClick(symbolSummary: SymbolSummary)
        fun onRemoveButtonClick(symbolSummary: SymbolSummary)
    }

    /**
     * Atualiza a lista filtrada de ações a ser apresentada no RecyclerView.
     */
    fun filter(filteredList: List<SymbolSummary>) {
        this.stockList = filteredList
        notifyDataSetChanged()
    }
}
