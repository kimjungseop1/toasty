package com.syncrown.arpang.ui.component.home.tab5_more.alert.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.databinding.ItemAlertListBinding
import com.syncrown.arpang.db.push_db.PushMessageEntity

class AlertListAdapter(
    private val context: Context,
    private val items: List<PushMessageEntity>
) : RecyclerView.Adapter<AlertListAdapter.AlertHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertHolder {
        return AlertHolder(
            ItemAlertListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlertHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AlertHolder(binding: ItemAlertListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val alertBinding: ItemAlertListBinding = binding
        fun onBind(context: Context, position: Int) {

            alertBinding.root.setOnClickListener {
                mListener?.onClick(position)
            }

            alertBinding.titleView.text = items[position].title

            alertBinding.contentView.text = items[position].body

            alertBinding.dateView.text = items[position].receiveTime.toString()

            Glide.with(context)
                .load(items[position].imageUrl)
                .into(alertBinding.contentImage)
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}