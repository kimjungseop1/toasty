package com.syncrown.toasty.ui.component.home.tab5_more.notice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.toasty.databinding.ItemNoticeBinding

class NoticeListAdapter(
    private val context: Context,
    private val sliderItems: ArrayList<String>
) : RecyclerView.Adapter<NoticeListAdapter.NoticeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        return NoticeHolder(
            ItemNoticeBinding.inflate(
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

    inner class NoticeHolder(binding: ItemNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemNoticeBinding = binding
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