package org.bandev.buddhaquotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_card.view.*

class ScrollingAdapter(

    private val scrollingList: List<ExampleItem>,
    private val listener: OnItemClickFinder,

    ) : RecyclerView.Adapter<ScrollingAdapter.ScrollingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_card,
            parent, false
        )

        return ScrollingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScrollingViewHolder, position: Int) {
        val currentItem = scrollingList[position]

        holder.textQuote.text = currentItem.quote
        holder.iconLike.setImageResource(currentItem.resource)

        if (currentItem.no_like) {
            holder.iconLike.setImageResource(R.drawable.heart_full_red)
        }
    }

    override fun getItemCount(): Int = scrollingList.size

    inner class ScrollingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        val textQuote: TextView = itemView.quote
        val iconLike: ImageView = itemView.like
        private val share: ImageView = itemView.share
        private val bin: ImageView = itemView.bin

        init {
            iconLike.setOnClickListener(this)
            share.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onShareClick(position)
                }
            }

            bin.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onBinClick(position, textQuote.text.toString())
                }
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onLikeClick(position, textQuote.text.toString())
            }
        }

        override fun onLongClick(view: View): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onLikeClick(position, textQuote.text.toString())
            }
            // Return true to indicate the click was handled
            return true
        }

    }

    interface OnItemClickFinder {
        fun onLikeClick(position: Int, text: String)

        fun onShareClick(position: Int)

        fun onBinClick(position: Int, text: String)
    }

}

