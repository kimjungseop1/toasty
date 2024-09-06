package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videoselect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.toasty.databinding.ItemGridVideoBinding
import com.syncrown.toasty.ui.commons.CommonFunc

class SelectGridItemAdapter(
    private val context: Context,
    private val items: List<VideoInfo>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<SelectGridItemAdapter.GridViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(videoInfo: VideoInfo)
    }

    inner class GridViewHolder(private val binding: ItemGridVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: List<VideoInfo>, position: Int) {

            Glide.with(context).load(item[position].filePath).into(binding.thumbnailView)

            binding.playTimeView.text = CommonFunc.convertMillisToMMSS(item[position].duration)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(item[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding =
            ItemGridVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(items, position)
    }

    override fun getItemCount(): Int = items.size
}

data class VideoInfo(
    val filePath: String,
    val duration: Long // Duration in milliseconds
)