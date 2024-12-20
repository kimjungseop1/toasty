package com.syncrown.arpang.ui.component.home.tab5_more.notice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemNoticeBinding
import com.syncrown.arpang.network.model.ResponseNoticeListDto
import java.util.concurrent.TimeUnit

class NoticeListAdapter(
    private val context: Context,
    private val items: ArrayList<ResponseNoticeListDto.Root>
) : RecyclerView.Adapter<NoticeListAdapter.NoticeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int, bbsid: String)
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
        return items.size
    }

    inner class NoticeHolder(private val binding: ItemNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(context: Context?, position: Int) {
            binding.root.setOnClickListener {
                mListener?.onClick(position, items[position].bbsid.toString())
            }

            binding.messageView.text = items[position].title
            binding.dateView.text = items[position].start_dt
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}