package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.toasty.databinding.ItemVideoEditMenuBinding

class VideoEditAdapter(
    private val items: ArrayList<EditItem>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<VideoEditAdapter.VideoEditViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class VideoEditViewHolder(
        private val context: Context,
        private val binding: ItemVideoEditMenuBinding,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }

        fun bind(item: EditItem) {
            Glide.with(context).load(item.imageResId).into(binding.itemIcon)
            binding.itemTitle.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoEditViewHolder {
        val context = parent.context
        val binding =
            ItemVideoEditMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoEditViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: VideoEditViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount(): Int = items.size
}

data class EditItem(val imageResId: Int, val text: String)
