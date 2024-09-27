package com.syncrown.toasty.ui.component.home.tab2_Lib.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.toasty.databinding.ItemMultiSpanBinding
import com.syncrown.toasty.databinding.ItemSingleSpanBinding

class LibGridItemAdapter(
    private val items: List<GridItem>,
    private val spanCount: Int,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    inner class SingleSpanViewHolder(private val binding: ItemSingleSpanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GridItem, position: Int) {
            binding.thumbnailView.setImageResource(item.imageResId)

            binding.root.setOnClickListener {
                listener.onItemClick(position)
            }

        }
    }

    inner class MultiSpanViewHolder(private val binding: ItemMultiSpanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GridItem, position: Int) {
            binding.thumbnailView.setImageResource(item.imageResId)

            binding.root.setOnClickListener {
                listener.onItemClick(position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding =
                ItemSingleSpanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SingleSpanViewHolder(binding)
        } else {
            val binding =
                ItemMultiSpanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MultiSpanViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is SingleSpanViewHolder) {
            holder.bind(item, position)
        } else if (holder is MultiSpanViewHolder) {
            holder.bind(item, position)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return if (spanCount == 1) 1 else 2
    }
}

data class GridItem(val imageResId: Int, val text: String)
