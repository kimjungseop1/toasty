package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videoselect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.toasty.databinding.ItemGridVideoBinding

class GridItemAdapter(private val items: List<GridItem>) :
    RecyclerView.Adapter<GridItemAdapter.GridViewHolder>() {

    inner class GridViewHolder(private val binding: ItemGridVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GridItem) {
            binding.thumbnailView.setImageResource(item.imageResId)
            binding.playTimeView.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = ItemGridVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

data class GridItem(val imageResId: Int, val text: String)
