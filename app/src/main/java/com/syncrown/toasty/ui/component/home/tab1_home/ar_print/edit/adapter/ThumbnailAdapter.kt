package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.toasty.databinding.ItemThumbnailBinding

class ThumbnailAdapter(
    private val context: Context,
    private val thumbnails: List<Bitmap>,
    private val listener: OnItemClickListener // 추가된 부분
) : RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ThumbnailViewHolder(val binding: ItemThumbnailBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailViewHolder {
        val binding = ItemThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThumbnailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThumbnailViewHolder, position: Int) {
        val bitmap = thumbnails[position]
        Glide.with(context)
            .load(bitmap)
            .into(holder.binding.itemThumb)
    }

    override fun getItemCount(): Int = thumbnails.size
}
