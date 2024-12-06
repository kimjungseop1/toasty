package com.syncrown.arpang.ui.component.home.tab3_share.detail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemDetailCommentBinding
import com.syncrown.arpang.network.model.ResponseCommentListDto

class DetailCommentListAdapter(
    private val context: Context,
    private val items: ArrayList<ResponseCommentListDto.Root>,
    private var mlistener: OnItemClickListener
) : RecyclerView.Adapter<DetailCommentListAdapter.SubscribeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int, view: View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mlistener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribeHolder {
        return SubscribeHolder(
            ItemDetailCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubscribeHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SubscribeHolder(private val binding: ItemDetailCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(context: Context?, position: Int) {
            binding.moreView.setOnClickListener {
                mlistener.onClick(position, binding.moreView)
            }

            if (items[position].nick_nm != null) {
                binding.nameView.text = items[position].nick_nm
            } else {
                binding.nameView.text = ""
            }

            binding.messageView.text = items[position].comment

            binding.dateView.text = items[position].save_ds

        }
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: ArrayList<ResponseCommentListDto.Root>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun addMoreData(data: java.util.ArrayList<ResponseCommentListDto.Root>) {
        if (data.size != 0) {
            val startPosition = items?.size ?: 0
            items?.addAll(data)
            notifyItemRangeInserted(startPosition, data.size)
        }
    }
}