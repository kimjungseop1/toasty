package com.syncrown.arpang.ui.component.home.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemSearchInputBinding
import com.syncrown.arpang.network.model.ResponseSearchingMatchDto

class ShareSearchingListAdapter(
    private val context: Context,
    private val items: ArrayList<ResponseSearchingMatchDto.Sub1>
) : RecyclerView.Adapter<ShareSearchingListAdapter.NoticeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int, hashtagNm: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        return NoticeHolder(
            ItemSearchInputBinding.inflate(
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

    inner class NoticeHolder(private val binding: ItemSearchInputBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(context: Context?, position: Int) {
            binding.root.setOnClickListener {
                mListener?.onClick(position, items[position].hashtag_nm.toString())
            }

            binding.searchText.text = items[position].hashtag_nm
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}