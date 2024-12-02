package com.syncrown.arpang.ui.component.home.tab3_share.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.databinding.ItemShareMultiBinding
import com.syncrown.arpang.databinding.ItemShareSingleBinding
import com.syncrown.arpang.network.model.ResponseShareContentAllOpenListDto

class ShareGridItemAdapter(
    private val context: Context,
    private val items: ArrayList<ResponseShareContentAllOpenListDto.Root>?,
    private val spanCount: Int,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, cntntsNo : String)
    }

    inner class SingleSpanViewHolder(private val binding: ItemShareSingleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseShareContentAllOpenListDto.Root, position: Int) {
            Glide.with(context)
                .load(item.img_url)
                .into(binding.thumbnailView)

            binding.root.setOnClickListener {
                listener.onItemClick(position, items?.get(position)?.cntnts_no.toString())
            }
        }
    }

    inner class MultiSpanViewHolder(private val binding: ItemShareMultiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseShareContentAllOpenListDto.Root, position: Int) {
            Glide.with(context)
                .load(item.img_url)
                .into(binding.thumbnailView)

            binding.root.setOnClickListener {
                listener.onItemClick(position, items?.get(position)?.cntnts_no.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding =
                ItemShareSingleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SingleSpanViewHolder(binding)
        } else {
            val binding =
                ItemShareMultiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun addMoreData(newData: ArrayList<ResponseShareContentAllOpenListDto.Root>) {
        if (newData.size != 0) {
            val startPosition = items?.size ?: 0
            items?.addAll(newData)
            notifyItemRangeInserted(startPosition, newData.size)
        }
    }
}
