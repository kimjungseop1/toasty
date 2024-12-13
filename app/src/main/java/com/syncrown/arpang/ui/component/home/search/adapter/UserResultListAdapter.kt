package com.syncrown.arpang.ui.component.home.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemMatchUserBinding
import com.syncrown.arpang.network.model.ResponseResultMatchDto

class UserResultListAdapter(
    private val context: Context,
    private val items: ArrayList<ResponseResultMatchDto.Root>
) : RecyclerView.Adapter<UserResultListAdapter.NoticeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        return NoticeHolder(
            ItemMatchUserBinding.inflate(
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

    inner class NoticeHolder(private val binding: ItemMatchUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(context: Context?, position: Int) {
            binding.root.setOnClickListener {
                mListener?.onClick(position)
            }

            binding.nameTxt.text = items[position].nick_nm
            binding.contentTxt.text = items[position].contents_cnt.toString() + "건의 공개된 게시글"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: ArrayList<ResponseResultMatchDto.Root>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(newData: ArrayList<ResponseResultMatchDto.Root>) {
        val startPos = items.size
        items.addAll(newData)
        notifyItemRangeInserted(startPos, newData.size)
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}