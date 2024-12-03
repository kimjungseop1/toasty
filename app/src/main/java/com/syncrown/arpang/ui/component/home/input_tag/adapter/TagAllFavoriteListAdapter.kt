package com.syncrown.arpang.ui.component.home.input_tag.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemTagUseBinding
import com.syncrown.arpang.network.model.ResponseAllFavoriteDto

class TagAllFavoriteListAdapter(
    private val context: Context,
    private val sliderItems: ArrayList<ResponseAllFavoriteDto.Root>
) : RecyclerView.Adapter<TagAllFavoriteListAdapter.NoticeHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int, text: String)
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

    inner class NoticeHolder(private val binding: ItemTagUseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(context: Context?, position: Int) {
            binding.root.setOnClickListener {
                mListener?.onClick(position, sliderItems[position].hashtag_nm.toString())
            }

            binding.tagView.text = "# " + sliderItems[position].hashtag_nm

            binding.tagCount.text = "전체 " + sliderItems[position].count.toString() + "건"
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}