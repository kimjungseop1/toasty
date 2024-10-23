package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.guide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.syncrown.arpang.databinding.ItemArGuideBinding

class ArGuidePagerAdapter(
    private val context: Context,
    viewPager: ViewPager2,
    private val images: List<Int>,
    private val onPageChangeListener: OnPageChangeListener
) : RecyclerView.Adapter<ArGuidePagerAdapter.ImageViewHolder>() {

    interface OnPageChangeListener {
        fun onPageChanged(currentPage: Int)
    }

    inner class ImageViewHolder(val binding: ItemArGuideBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemArGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context)
            .load(images[position])
            .into(holder.binding.itemGuideImg)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                onPageChangeListener.onPageChanged(position)
            }
        })
    }
}