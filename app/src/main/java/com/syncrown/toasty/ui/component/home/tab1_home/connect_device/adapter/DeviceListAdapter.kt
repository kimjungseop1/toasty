package com.syncrown.toasty.ui.component.home.tab1_home.connect_device.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ItemSearchDeviceBinding

class DeviceListAdapter(
    private val context: Context,
    private val items: ArrayList<String>
) : RecyclerView.Adapter<DeviceListAdapter.DeviceListHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceListHolder {
        return DeviceListHolder(
            ItemSearchDeviceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DeviceListHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class DeviceListHolder(binding: ItemSearchDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemSearchDeviceBinding = binding
        fun onBind(context: Context, position: Int) {
            // 데이터 바인딩 로직
            binding.root.setOnClickListener {
                mListener?.onClick(position)
            }

            if (items[position].contains("TOASTY")) {
                Glide.with(context).load(R.drawable.print_connect_tosty).into(binding.itemPrintImg)
            } else {
                Glide.with(context).load(R.drawable.print_connect_oveny).into(binding.itemPrintImg)
            }

            binding.itemPrintName.text = items[position]
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}