package com.syncrown.arpang.ui.component.home.tab1_home.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemMainEventBinding

class MainEventAdapter(
    private val context: Context,
    private val sliderItems: ArrayList<String>
) : RecyclerView.Adapter<MainEventAdapter.SlideBannerHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideBannerHolder {
        return SlideBannerHolder(
            ItemMainEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SlideBannerHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class SlideBannerHolder(binding: ItemMainEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val eventBinding: ItemMainEventBinding = binding
        fun onBind(context: Context, position: Int) {
            eventBinding.root.setOnClickListener {
                mListener?.onClick(position)
            }

            Glide.with(context)
                .load(R.drawable.sample_img_1)
                .circleCrop()
                .into(eventBinding.itemImage)
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}