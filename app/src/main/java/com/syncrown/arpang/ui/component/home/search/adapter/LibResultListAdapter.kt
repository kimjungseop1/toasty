package com.syncrown.arpang.ui.component.home.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.databinding.ItemMatchUserBinding
import com.syncrown.arpang.databinding.ItemShareMultiBinding
import com.syncrown.arpang.network.model.ResponseResultMatchDto

class LibResultListAdapter(
    private val context: Context,
    private val items: ArrayList<ResponseResultMatchDto.Sub2>
) : RecyclerView.Adapter<LibResultListAdapter.NoticeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int, cntntsNo : String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        return NoticeHolder(
            ItemShareMultiBinding.inflate(
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

    inner class NoticeHolder(private val binding: ItemShareMultiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(context: Context, position: Int) {
            binding.root.setOnClickListener {
                mListener?.onClick(position, items[position].cntnts_no.toString())
            }

            Glide.with(context)
                .load(items[position].img_url)
                .into(binding.thumbnailView)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: ArrayList<ResponseResultMatchDto.Sub2>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(newData: ArrayList<ResponseResultMatchDto.Sub2>) {
        val startPosition = items.size
        items.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}