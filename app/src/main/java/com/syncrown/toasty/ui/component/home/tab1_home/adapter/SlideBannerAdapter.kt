package com.syncrown.toasty.ui.component.home.tab1_home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.syncrown.toasty.databinding.ItemHomeBannerBinding

class SlideBannerAdapter(
    private val context: Context,
    viewPager: ViewPager2,
    private val sliderItems: ArrayList<String>
) :
    RecyclerView.Adapter<SlideBannerAdapter.SlideBannerHolder>() {
    private val viewPager2: ViewPager2 = viewPager

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    val sliderItemsSize: Int
        get() = sliderItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideBannerHolder {
        return SlideBannerHolder(
            ItemHomeBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SlideBannerHolder, position: Int) {
        holder.onBind(context, position)
        if (position == sliderItems.size - 2) {
            viewPager2.post(holder.runnable)
        }
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class SlideBannerHolder(binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        private val bannerBinding: ItemHomeBannerBinding = binding
        fun onBind(context: Context?, position: Int) {
//            bannerBinding.bannerImg.text = sliderItems[position]
        }

        @SuppressLint("NotifyDataSetChanged")
        val runnable = Runnable {
            sliderItems.addAll(sliderItems)
            notifyDataSetChanged()
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}