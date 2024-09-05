package com.syncrown.toasty.ui.component.home.tab2_Lib.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.toasty.databinding.ItemGridLibBinding

class LibLinearItemAdapter(private var items: ArrayList<String>) :
    RecyclerView.Adapter<LibLinearItemAdapter.LibLinearHolder>() {
    inner class LibLinearHolder(private val binding: ItemGridLibBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, position: Int) {
            binding.playTimeView.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibLinearHolder {
        val binding = ItemGridLibBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LibLinearHolder(binding)
    }

    override fun onBindViewHolder(holder: LibLinearHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}