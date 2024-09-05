package com.syncrown.toasty.ui.component.home.tab2_Lib.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.toasty.databinding.ItemGridLibBinding

class LibGridItemAdapter(private val items: List<GridItem>) :
    RecyclerView.Adapter<LibGridItemAdapter.GridViewHolder>() {

    inner class GridViewHolder(private val binding: ItemGridLibBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GridItem) {
            binding.thumbnailView.setImageResource(item.imageResId)
            binding.playTimeView.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = ItemGridLibBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

data class GridItem(val imageResId: Int, val text: String)
