package com.syncrown.arpang.ui.component.home.input_tag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemNoticeBinding
import com.syncrown.arpang.databinding.ItemTagUseBinding

class TagUseListAdapter(
    private val context: Context,
    private val sliderItems: ArrayList<String>
) : RecyclerView.Adapter<TagUseListAdapter.NoticeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        return NoticeHolder(
            ItemTagUseBinding.inflate(
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

    inner class NoticeHolder(binding: ItemTagUseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemTagUseBinding = binding
        fun onBind(context: Context?, position: Int) {
            // 데이터 바인딩 로직
            binding.root.setOnClickListener {
                mListener?.onClick(position)
            }
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}