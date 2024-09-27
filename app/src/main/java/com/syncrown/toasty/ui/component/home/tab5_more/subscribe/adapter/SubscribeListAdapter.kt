package com.syncrown.toasty.ui.component.home.tab5_more.subscribe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.toasty.databinding.ItemNoticeBinding
import com.syncrown.toasty.databinding.ItemSubscribeBinding

class SubscribeListAdapter(
    private val context: Context,
    private val items: ArrayList<String>,
    private var mlistener: OnItemClickListener
) : RecyclerView.Adapter<SubscribeListAdapter.SubscribeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int, view: View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mlistener = listener
    }

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
        fun onBind(context: Context?, position: Int) {
            // 데이터 바인딩 로직
            binding.moreView.setOnClickListener {
                mlistener?.onClick(position, binding.moreView)
            }
        }
    }
}