package com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemCartridgeMenuBinding
import com.syncrown.arpang.network.model.ResponseRecommendTagListDto

class CartridgeMenuListAdapter(
    private val context: Context,
    private val items: ResponseRecommendTagListDto
) : RecyclerView.Adapter<CartridgeMenuListAdapter.CartridgeMenuHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
        listener?.onClick(selectedPosition)
    }

    private var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartridgeMenuHolder {
        return CartridgeMenuHolder(
            ItemCartridgeMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartridgeMenuHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return items.root.size
    }

    inner class CartridgeMenuHolder(private val binding: ItemCartridgeMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(context: Context?, position: Int) {
            if (position == selectedPosition) {
                binding.root.backgroundTintList = context?.getColorStateList(R.color.color_8e5d4b)
                binding.itemMenuView.setTextColor(context!!.getColor(R.color.color_white))
            } else {
                binding.root.backgroundTintList = context?.getColorStateList(R.color.color_F0F0F0)
                binding.itemMenuView.setTextColor(context!!.getColor(R.color.color_5B5B5B))
            }

            binding.itemMenuView.text = items.root[position].tag_nm

            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition)
                notifyItemChanged(position)
                mListener?.onClick(position)
            }
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}