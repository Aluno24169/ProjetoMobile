package com.example.ProjetoMobileStocks.ui.News

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ProjetoMobileStocks.R
import com.squareup.picasso.Picasso
import retrofit.models.News

/**
 * Adaptador de RecyclerView para a apresentação gráfica dos cartões de notícias do mercado financeiro.
 */
class NewsRecyclerAdapter(private var stockList: List<News>) :
    RecyclerView.Adapter<NewsRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_news_card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val newsItem = stockList[position]
        holder.date.text = newsItem.date
        holder.description.text = newsItem.description
        holder.title.text = newsItem.title

        // Calcula dinamicamente o número de linhas para alternar o botão "Read More / Read Less"
        holder.description.post {
            val layout = holder.description.layout
            if (layout != null) {
                val lines = layout.lineCount
                if (lines > 0) {
                    val ellipsisCount = layout.getEllipsisCount(lines - 1)
                    if (ellipsisCount > 0) {
                        holder.readMoreButton.visibility = View.VISIBLE
                        holder.description.maxLines = 3

                        holder.readMoreButton.setOnClickListener {
                            if (holder.description.maxLines == 3) {
                                holder.description.maxLines = Int.MAX_VALUE
                                holder.readMoreButton.text = holder.itemView.context.getString(R.string.read_less)
                            } else {
                                holder.description.maxLines = 3
                                holder.readMoreButton.text = holder.itemView.context.getString(R.string.read_more)
                            }
                        }
                    } else {
                        holder.readMoreButton.visibility = View.GONE
                    }
                }
            }
        }

        // Carrega a imagem associada à notícia via Picasso com tratamento de erro e placeholder
        if (newsItem.image_url.isNotEmpty()) {
            Picasso.get()
                .load(newsItem.image_url)
                .placeholder(R.drawable.ic_home_black_24dp)
                .error(R.drawable.ic_home_black_24dp)
                .fit()
                .centerCrop()
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.ic_home_black_24dp)
        }
    }

    /**
     * ViewHolder para mapeamento dos componentes de cada cartão de notícia.
     */
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.tvDate)
        val description: TextView = itemView.findViewById(R.id.tvDesc)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val imageView: ImageView = itemView.findViewById(R.id.ivImage)
        val readMoreButton: Button = itemView.findViewById(R.id.btnReadMore)
    }
}
