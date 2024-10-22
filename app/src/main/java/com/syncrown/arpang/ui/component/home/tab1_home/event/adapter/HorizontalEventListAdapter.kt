package com.syncrown.arpang.ui.component.home.tab1_home.event.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemHorizontalEvnetBinding

class HorizontalEventListAdapter(
    private val items: ArrayList<String>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<HorizontalEventListAdapter.HorizontalEventViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    class HorizontalEventViewHolder(
        private val context: Context,
        private val binding: ItemHorizontalEvnetBinding,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                listener.onItemClick(position)
            }
        }

        fun bind() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalEventViewHolder {
        val context = parent.context
        val binding =
            ItemHorizontalEvnetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorizontalEventViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: HorizontalEventViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = items.size
}