package com.syncrown.arpang.ui.component.home.tab5_more.event.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemEventAllBinding

class EventAllListAdapter(
    private val context: Context,
    private val sliderItems: ArrayList<String>
) : RecyclerView.Adapter<EventAllListAdapter.NoticeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        return NoticeHolder(
            ItemEventAllBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoticeHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class NoticeHolder(binding: ItemEventAllBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemEventAllBinding = binding
        fun onBind(context: Context, position: Int) {

            binding.root.setOnClickListener {
                mListener?.onClick(position)
            }
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}