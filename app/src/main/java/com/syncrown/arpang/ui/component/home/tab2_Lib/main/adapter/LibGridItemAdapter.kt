package com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.databinding.ItemMultiSpanBinding
import com.syncrown.arpang.databinding.ItemSingleSpanBinding
import com.syncrown.arpang.network.model.ResponseStorageContentListDto

class LibGridItemAdapter(
    private val context: Context,
    private val items: ArrayList<ResponseStorageContentListDto.ROOT>?,
    private val spanCount: Int,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, cntntsNo: String)
    }

    inner class SingleSpanViewHolder(private val binding: ItemSingleSpanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseStorageContentListDto.ROOT, position: Int) {
            Glide.with(context)
                .load(item.img_url)
                .into(binding.thumbnailView)

            binding.root.setOnClickListener {
                listener.onItemClick(position, item.cntnts_no.toString())
            }
        }
    }

    inner class MultiSpanViewHolder(private val binding: ItemMultiSpanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseStorageContentListDto.ROOT, position: Int) {
            Glide.with(context)
                .load(item.img_url)
                .into(binding.thumbnailView)

            binding.root.setOnClickListener {
                listener.onItemClick(position, item.cntnts_no.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding =
                ItemSingleSpanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SingleSpanViewHolder(binding)
        } else {
            val binding =
                ItemMultiSpanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MultiSpanViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items?.get(position) ?: return

        if (holder is SingleSpanViewHolder) {
            holder.bind(item, position)
        } else if (holder is MultiSpanViewHolder) {
            holder.bind(item, position)
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return if (spanCount == 1) 1 else 2
    }

    fun addMoreData(newData: ArrayList<ResponseStorageContentListDto.ROOT>) {
        if (newData.size != 0) {
            val startPosition = items?.size ?: 0
            items?.addAll(newData)
            notifyItemRangeInserted(startPosition, newData.size)
        }
    }
}
