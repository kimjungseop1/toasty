package com.syncrown.arpang.ui.component.home.tab5_more.block.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemCutOffBinding

class BlockUserAdapter(
    private val items: ArrayList<String>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<BlockUserAdapter.CutOffViewHolder>() {

    private val selectedPositions: MutableSet<Int> = mutableSetOf()

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    class CutOffViewHolder(
        private val context: Context,
        private val binding: ItemCutOffBinding,
        private val listener: OnItemClickListener,
        private val adapter: BlockUserAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.unblockingBtn.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (adapter.selectedPositions.contains(position)) {
                        adapter.selectedPositions.remove(position)
                    } else {
                        adapter.selectedPositions.add(position)
                    }
                    adapter.notifyItemChanged(position)
                    listener.onItemClick(position)
                }
            }
        }

        fun bind(item: String, isSelected: Boolean) {
            binding.blockingTxt.visibility = if (isSelected) View.VISIBLE else View.GONE
            binding.unblockingBtn.visibility = if (isSelected) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CutOffViewHolder {
        val context = parent.context
        val binding =
            ItemCutOffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CutOffViewHolder(context, binding, listener, this)
    }

    override fun onBindViewHolder(holder: CutOffViewHolder, position: Int) {
        val isSelected = selectedPositions.contains(position)
        holder.bind(items[position], isSelected)
    }

    override fun getItemCount(): Int = items.size
}