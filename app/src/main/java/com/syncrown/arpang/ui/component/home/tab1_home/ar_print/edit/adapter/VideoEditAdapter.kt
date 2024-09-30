package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemVideoEditMenuBinding

class VideoEditAdapter(
    private val items: ArrayList<NavBarItem>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<VideoEditAdapter.VideoEditViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    class VideoEditViewHolder(
        private val context: Context,
        private val binding: ItemVideoEditMenuBinding,
        private val listener: OnItemClickListener,
        private val adapter: VideoEditAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (adapter.selectedPosition == position) {
                        // Deselect item
                        adapter.selectedPosition = RecyclerView.NO_POSITION
                    } else {
                        // Select new item
                        adapter.selectedPosition = position
                    }
                    adapter.notifyDataSetChanged()
                    listener.onItemClick(position)
                }
            }
        }

        fun bind(item: NavBarItem, isSelected: Boolean) {
            val imageResId = if (isSelected) {
                item.selectedImageResId  // 선택되었을 때의 이미지 리소스 ID
            } else {
                item.imageResId  // 기본 이미지 리소스 ID
            }

            Glide.with(context).load(imageResId).into(binding.itemIcon)
            binding.itemTitle.text = item.text

            // Change the text color based on selection state
            binding.itemTitle.setTextColor(
                if (isSelected) ContextCompat.getColor(
                    context,
                    R.color.color_DDE02E
                ) else ContextCompat.getColor(context, R.color.color_white)
            )

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoEditViewHolder {
        val context = parent.context
        val binding =
            ItemVideoEditMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoEditViewHolder(context, binding, listener, this)
    }

    override fun onBindViewHolder(holder: VideoEditViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(items[position], isSelected)
    }

    override fun getItemCount(): Int = items.size
}

data class NavBarItem(
    val imageResId: Int,
    val selectedImageResId: Int,
    val text: String
)
