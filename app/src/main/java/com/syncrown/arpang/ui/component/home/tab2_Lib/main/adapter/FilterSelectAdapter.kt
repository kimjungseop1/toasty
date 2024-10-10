package com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemCategorySelectedBinding

class FilterSelectAdapter(
    private val context: Context,
    private var items: List<String>,
    private val onItemClicked: (String) -> Unit
) :
    RecyclerView.Adapter<FilterSelectAdapter.SelectedItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedItemViewHolder {
        val binding =
            ItemCategorySelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SelectedItemViewHolder(private val binding: ItemCategorySelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.selectedItem.text = item

            binding.deleteBtn.setOnClickListener {
                onItemClicked(item)
            }

        }
    }

    fun getSelectItemSize(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}