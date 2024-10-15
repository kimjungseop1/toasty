package com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemFontBinding
import com.syncrown.arpang.ui.commons.FontItem

class FontAdapter(
    private val context: Context,
    private val fontItems: List<FontItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FontAdapter.FontViewHolder>() {

    private var selectedPosition: Int = -1

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, typeface: Typeface)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val binding = ItemFontBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FontViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        val fontItem = fontItems[position]
        holder.bind(fontItem, position)
    }

    override fun getItemCount(): Int = fontItems.size

    inner class FontViewHolder(
        private val binding: ItemFontBinding,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(fontItem: FontItem, position: Int) {
            binding.fontName.text = fontItem.text
            binding.fontName.typeface = fontItem.typeface

            if (position == selectedPosition) {
                binding.fontName.setTextColor(ContextCompat.getColor(context, R.color.color_DDE02E))
            } else {
                binding.fontName.setTextColor(ContextCompat.getColor(context, R.color.color_white))
            }

            binding.root.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    selectedPosition = position
                    listener.onItemClick(position, fontItem.typeface)

                    notifyDataSetChanged()
                }
            }
        }
    }
}


