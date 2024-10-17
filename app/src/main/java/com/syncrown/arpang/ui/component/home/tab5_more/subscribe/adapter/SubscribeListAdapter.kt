package com.syncrown.arpang.ui.component.home.tab5_more.subscribe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemSubscribeBinding
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.SubscribeType

class SubscribeListAdapter(
    private val context: Context,
    private val items: ArrayList<String>,
    private val type: SubscribeType,
    private var mDelListener: OnItemDeleteListener,
    private var mListener: OnItemClickListener
) : RecyclerView.Adapter<SubscribeListAdapter.SubscribeHolder>() {

    interface OnItemDeleteListener {
        fun onDelete(position: Int, view: View)
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

//    fun setOnItemDeleteListener(listener: OnItemDeleteListener) {
//        mDelListener = listener
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        mListener = listener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribeHolder {
        return SubscribeHolder(
            ItemSubscribeBinding.inflate(
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

    inner class SubscribeHolder(binding: ItemSubscribeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemSubscribeBinding = binding
        fun onBind(context: Context, position: Int) {
            if (type == SubscribeType.MY) {
                binding.moreView.visibility = View.GONE
            } else {
                binding.moreView.visibility = View.VISIBLE
            }

            binding.moreView.setOnClickListener {
                mDelListener.onDelete(position, binding.moreView)
            }

            binding.root.setOnClickListener {
                mListener.onClick(position)
            }
        }
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }
}