package com.syncrown.arpang.ui.component.home.tab1_home.connect_device.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemSearchDeviceBinding

class DeviceListAdapter(
    private val context: Context,
    private val items: MutableList<BluetoothDevice>
) : RecyclerView.Adapter<DeviceListAdapter.DeviceListHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int, name: String, address: String)
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

    inner class DeviceListHolder(private val binding: ItemSearchDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("MissingPermission")
        fun onBind(context: Context, position: Int) {
            binding.root.setOnClickListener {
                mListener?.onClick(position, items[position].name ?: "Unknown Device", items[position].address)
            }

//            if (items[position].contains("TOASTY")) {
//                Glide.with(context).load(R.drawable.print_connect_tosty).into(binding.itemPrintImg)
//            } else {
//                Glide.with(context).load(R.drawable.print_connect_oveny).into(binding.itemPrintImg)
//            }

            Glide.with(context).load(R.drawable.print_connect_tosty).into(binding.itemPrintImg)

            binding.itemPrintName.text = items[position].name ?: "Unknown Device"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: BluetoothDevice) {
        if (!items.any { it.address == item.address }) {
            items.add(item)
            notifyItemInserted(items.size - 1)
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}