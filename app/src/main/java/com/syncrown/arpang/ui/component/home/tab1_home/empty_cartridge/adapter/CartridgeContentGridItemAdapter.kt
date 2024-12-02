package com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemCatridgeContentBinding
import com.syncrown.arpang.network.model.ResponseCartridgeListByTagDto

class CartridgeContentGridItemAdapter(
    private val items: ResponseCartridgeListByTagDto,
    private val clickListener: OnItemClickListener,
    private val scaleListener: OnScaleClickListener
) : RecyclerView.Adapter<CartridgeContentGridItemAdapter.MultiSpanViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnScaleClickListener {
        fun onScaleClick(position: Int)
    }

    inner class MultiSpanViewHolder(private val binding: ItemCatridgeContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseCartridgeListByTagDto.Root, position: Int) {
            binding.itemPaper.setOnClickListener {
                clickListener.onItemClick(position)
            }

            binding.itemScaleBtn.setOnClickListener {
                scaleListener.onScaleClick(position)
            }

            binding.nameView.text = item.ctge_nm
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartridgeContentGridItemAdapter.MultiSpanViewHolder {
        val binding =
            ItemCatridgeContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MultiSpanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MultiSpanViewHolder, position: Int) {
        val item = items.root[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int = items.root.size
}
