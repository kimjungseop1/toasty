package com.syncrown.arpang.ui.component.home.tab1_home.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemHomeBannerBinding
import com.syncrown.arpang.network.model.ResponseMainBannerDto

class SlideBannerAdapter(
    private val context: Context,
    viewPager: ViewPager2,
    private val sliderItems: ArrayList<ResponseMainBannerDto.Root>,
    private val onPageChangeListener: OnPageChangeListener
) : RecyclerView.Adapter<SlideBannerAdapter.SlideBannerHolder>() {
    private val viewPager2: ViewPager2 = viewPager

    interface OnItemClickListener {
        fun onClick(position: Int, clickLink: String)
    }

    interface OnPageChangeListener {
        fun onPageChanged(currentPage: Int, totalItems: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

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
        holder.onBind(context, position % sliderItems.size)
        if (position == sliderItems.size - 2) {
            viewPager2.post(holder.runnable)
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    inner class SlideBannerHolder(binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val bannerBinding: ItemHomeBannerBinding = binding
        fun onBind(context: Context, position: Int) {
            bannerBinding.bannerImg.setOnClickListener {
                mListener?.onClick(position, sliderItems[position].click_link.toString())
            }

            Glide.with(context)
                .load(sliderItems[position].file_path)
                .into(bannerBinding.bannerImg)
        }

        val runnable = Runnable {
            viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }

    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentPage = position % sliderItems.size
                onPageChangeListener.onPageChanged(currentPage + 1, sliderItems.size)
            }
        })
    }
}