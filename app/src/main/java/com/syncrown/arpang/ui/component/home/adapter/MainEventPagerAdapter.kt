package com.syncrown.arpang.ui.component.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemMainEventPagerBinding

class MainEventPagerAdapter(
    private val context: Context,
    private val items: List<String>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MainEventPagerAdapter.ViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var currentPage: Int = 0

    class ViewHolder(
        private val binding: ItemMainEventPagerBinding,
        private val context: Context,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String, position: Int) {
            Glide.with(context)
                .load(R.drawable.sample_img_1)
                .into(binding.bannerImg)

            binding.root.setOnClickListener {
                listener.onItemClick(position)  // Pass the position to the listener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMainEventPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)  // Pass the position here
    }

    override fun getItemCount(): Int = items.size

    fun updateCurrentPage(page: Int) {
        currentPage = page
        notifyDataSetChanged()
    }

    fun getPageInfo(): String {
        return "${currentPage + 1}/${itemCount}"
    }
}
