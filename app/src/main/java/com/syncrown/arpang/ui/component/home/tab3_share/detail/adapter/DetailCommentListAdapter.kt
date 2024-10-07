package com.syncrown.arpang.ui.component.home.tab3_share.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemDetailCommentBinding
import com.syncrown.arpang.databinding.ItemSubscribeBinding

class DetailCommentListAdapter(
    private val context: Context,
    private val items: ArrayList<String>,
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

    inner class SubscribeHolder(binding: ItemDetailCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemDetailCommentBinding = binding
        fun onBind(context: Context?, position: Int) {
            // 데이터 바인딩 로직
            binding.moreView.setOnClickListener {
                mlistener.onClick(position, binding.moreView)
            }
        }
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }
}