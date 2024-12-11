package com.syncrown.arpang.ui.component.home.tab5_more.block.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ItemCutOffBinding
import com.syncrown.arpang.network.model.ResponseBlockUserListDto

class BlockUserAdapter(
    private val items: ArrayList<ResponseBlockUserListDto.Root>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<BlockUserAdapter.CutOffViewHolder>() {

    private val selectedPositions: MutableSet<Int> = mutableSetOf()

    fun interface OnItemClickListener {
        fun onBlockClick(position: Int, blockUserId: String)
    }

    @SuppressLint("NotifyDataSetChanged")
    class CutOffViewHolder(
        private val binding: ItemCutOffBinding,
        private val listener: OnItemClickListener,
        private val adapter: BlockUserAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseBlockUserListDto.Root, isSelected: Boolean) {
            binding.unblockingBtn.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (adapter.selectedPositions.contains(position)) {
                        adapter.selectedPositions.remove(position)
                    } else {
                        adapter.selectedPositions.add(position)
                    }
                    adapter.notifyItemChanged(position)
                    listener.onBlockClick(position, item.block_user_id.toString())
                }
            }

            binding.blockingTxt.visibility = if (isSelected) View.VISIBLE else View.GONE
            binding.unblockingBtn.visibility = if (isSelected) View.GONE else View.VISIBLE

            binding.nameTxt.text = item.block_nick_nm
            binding.bodyTxt.text = item.block_user_share_cnt.toString() + "건의 공개된 게시글"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CutOffViewHolder {
        val binding =
            ItemCutOffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CutOffViewHolder(binding, listener, this)
    }

    override fun onBindViewHolder(holder: CutOffViewHolder, position: Int) {
        val isSelected = selectedPositions.contains(position)
        holder.bind(items[position], isSelected)
    }

    override fun getItemCount(): Int = items.size

    fun addMoreData(data: ArrayList<ResponseBlockUserListDto.Root>) {
        if (data.size != 0) {
            val startPosition = items.size
            items.addAll(data)
            notifyItemRangeInserted(startPosition, data.size)
        }
    }

    fun updateItem(position: Int) {
        //selectedPositions.remove(position)
        notifyItemChanged(position)
    }

}