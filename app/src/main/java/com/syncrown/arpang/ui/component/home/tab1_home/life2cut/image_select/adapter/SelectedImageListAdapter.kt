package com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.syncrown.arpang.databinding.ItemSelectedImageBinding

class SelectedImageListAdapter(
    private val context: Context,
    private val items: MutableList<ImageInfo>
) : RecyclerView.Adapter<SelectedImageListAdapter.ImageListHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(imageInfo: ImageInfo)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListHolder {
        return ImageListHolder(
            ItemSelectedImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageListHolder, position: Int) {
        val imageInfo = items[position]
        holder.onBind(context, imageInfo)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ImageListHolder(private val binding: ItemSelectedImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun onBind(context: Context, imageInfo: ImageInfo) {
            binding.deleteBtn.setOnClickListener {
                mListener?.onClick(imageInfo)
            }

            Glide.with(context)
                .load(imageInfo.filePath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.thumbnailView)
        }
    }
}