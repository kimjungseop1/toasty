package com.syncrown.arpang.ui.component.home.tab5_more.cutoff

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemCutOffBinding

class CutOffAdapter(
    private val items: ArrayList<String>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<CutOffAdapter.CutOffViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    class CutOffViewHolder(
        private val context: Context,
        private val binding: ItemCutOffBinding,
        private val listener: OnItemClickListener,
        private val adapter: CutOffAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.unblockingBtn.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (adapter.selectedPosition == position) {
                        // Deselect item
                        adapter.selectedPosition = RecyclerView.NO_POSITION
                    } else {
                        // Select new item
                        adapter.selectedPosition = position
                    }
                    adapter.notifyDataSetChanged()
                    listener.onItemClick(position)
                }
            }
        }

        fun bind(item: String, isSelected: Boolean) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CutOffViewHolder {
        val context = parent.context
        val binding =
            ItemCutOffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CutOffViewHolder(context, binding, listener, this)
    }

    override fun onBindViewHolder(holder: CutOffViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(items[position], isSelected)
    }

    override fun getItemCount(): Int = items.size
}