package com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.syncrown.arpang.databinding.ItemGridImageBinding

class ImageSelectGridItemAdapter(
    private val context: Context,
    private val items: List<ImageInfo>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ImageSelectGridItemAdapter.GridViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(imageInfo: ImageInfo)
    }

    inner class GridViewHolder(private val binding: ItemGridImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: List<ImageInfo>, position: Int) {

            Glide.with(context)
                .load(item[position].filePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.thumbnailView)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(item[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding =
            ItemGridImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(items, position)
    }

    override fun getItemCount(): Int = items.size
}

data class ImageInfo(
    val filePath: String
)