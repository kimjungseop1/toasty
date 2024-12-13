package com.syncrown.arpang.ui.component.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.syncrown.arpang.databinding.ItemMainEventPagerBinding
import com.syncrown.arpang.network.model.ResponseMainBannerDto

class MainEventPagerAdapter(
    private val items: List<ResponseMainBannerDto.Root>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MainEventPagerAdapter.ViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, clickLink: String)
    }

    private var currentPage: Int = 0

    class ViewHolder(
        private val binding: ItemMainEventPagerBinding,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResponseMainBannerDto.Root, position: Int) {
            Glide.with(binding.root.context)
                .load(item.file_path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.bannerImg)

            binding.root.setOnClickListener {
                listener.onItemClick(position, item.click_link.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainEventPagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateCurrentPage(page: Int) {
        currentPage = page
        notifyDataSetChanged()
    }

    fun getPageInfo(): String {
        return "${currentPage + 1}/${itemCount}"
    }
}