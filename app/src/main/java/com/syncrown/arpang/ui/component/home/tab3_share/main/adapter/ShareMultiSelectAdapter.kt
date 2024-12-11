package com.syncrown.arpang.ui.component.home.tab3_share.main.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemCategoryMultiBinding

class ShareMultiSelectAdapter(
    private val context: Context,
    private val itemList: List<String>,
    private val itemSelectedListener: OnItemSelectedListener?
) : RecyclerView.Adapter<ShareMultiSelectAdapter.ViewHolder>() {

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int, isSelected: Boolean)
    }

    // 첫 번째 아이템을 기본으로 선택된 상태로 추가
    private val selectedItems = ArrayList<Int>().apply {
        add(0)  // 첫 번째 아이템 선택
    }
    private var lastSubmittedItems = ArrayList(selectedItems)

    init {
        // 어댑터가 초기화될 때 첫 번째 아이템이 선택된 상태임을 알림
        itemSelectedListener?.onItemSelected(0, true)
    }

    class ViewHolder(val binding: ItemCategoryMultiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryMultiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.contentView.text = item

        if (selectedItems.contains(position)) {
            holder.binding.contentView.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_8e5d4b
                )
            )

            holder.binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_f5f5f5
                )
            )

            holder.binding.contentView.typeface = Typeface.DEFAULT_BOLD

            Glide.with(context)
                .load(R.drawable.category_slected)
                .into(holder.binding.contentImage)
        } else {
            holder.binding.contentView.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_black
                )
            )

            holder.binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_white
                )
            )

            holder.binding.contentView.typeface = Typeface.DEFAULT

            Glide.with(context)
                .load(R.drawable.category_normal)
                .into(holder.binding.contentImage)
        }

        if (position == 0) {
            holder.binding.divider.visibility = View.VISIBLE
        } else {
            holder.binding.divider.visibility = View.GONE
        }

        // 아이템 클릭 시 선택 및 해제 처리
        holder.itemView.setOnClickListener {
            if (position == 0) {
                // 0번째 아이템 선택 시 나머지 선택 해제
                selectedItems.clear()
                selectedItems.add(0)
            } else {
                if (selectedItems.contains(0)) {
                    // 다른 아이템 선택 시 0번째 아이템 선택 해제
                    selectedItems.remove(0)
                }
                if (selectedItems.contains(position)) {
                    // 이미 선택된 아이템 해제
                    selectedItems.remove(position)
                } else {
                    // 선택되지 않은 아이템 선택
                    selectedItems.add(position)
                }
            }
            notifyDataSetChanged()
            itemSelectedListener?.onItemSelected(position, selectedItems.contains(position))
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getSelectedItemCount() : Int {
        return selectedItems.size
    }

    fun getSelectedItems(): List<Int> {
        return selectedItems
    }

    fun saveCurrentSelection() {
        lastSubmittedItems.clear()
        lastSubmittedItems.addAll(selectedItems)
    }

    fun restoreLastSubmittedSelection() {
        selectedItems.clear()
        selectedItems.addAll(lastSubmittedItems)
        notifyDataSetChanged()
    }
}
