package com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemCategoryChildBinding
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.Child

class FilterChildAdapter(
    private val context: Context,
    private var children: List<Child>
) : RecyclerView.Adapter<FilterChildAdapter.ChildViewHolder>() {

    private val selectedPositions = mutableSetOf<Int>()
    private var selectionChangedListener: ((Set<Int>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding =
            ItemCategoryChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bind(children[position], position)
    }

    override fun getItemCount(): Int = children.size

    inner class ChildViewHolder(private val binding: ItemCategoryChildBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(child: Child, position: Int) {
            binding.childTitle.text = child.childName

            binding.divider.visibility = if (position == 0) View.VISIBLE else View.GONE

            val isSelected = selectedPositions.contains(bindingAdapterPosition)
            if (isSelected) {
                binding.childTitle.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_8e5d4b
                    )
                )
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_f5f5f5
                    )
                )
                binding.childTitle.typeface = Typeface.DEFAULT_BOLD
                Glide.with(context).load(R.drawable.category_slected).into(binding.contentImage)
            } else {
                binding.childTitle.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_black
                    )
                )
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_white
                    )
                )
                binding.childTitle.typeface = Typeface.DEFAULT
                Glide.with(context).load(R.drawable.category_normal).into(binding.contentImage)
            }

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectedPositions.clear()
                    selectedPositions.add(position)

//                    if (position == 0) {
//                        selectedPositions.clear()
//                        selectedPositions.add(0)
//                    } else {
//                        selectedPositions.remove(0)
//                        if (selectedPositions.contains(position)) {
//                            selectedPositions.remove(position)
//                        } else {
//                            selectedPositions.add(position)
//                        }
//                    }
                    notifyDataSetChanged()
                    selectionChangedListener?.invoke(selectedPositions)
                }
            }
        }
    }

    fun getPositionOfChild(childName: String): Int {
        return children.indexOfFirst { it.childName == childName }
    }

    // 특정 위치를 선택 해제하는 메서드 추가
    @SuppressLint("NotifyDataSetChanged")
    fun deselectPosition(position: Int) {
        selectedPositions.remove(position)
        notifyDataSetChanged()
        selectionChangedListener?.invoke(selectedPositions)
    }

    fun setOnSelectionChangedListener(listener: (Set<Int>) -> Unit) {
        selectionChangedListener = listener
    }

    fun getChildName(position: Int): String {
        return if (position in children.indices) children[position].childName else ""
    }

    fun getSelectedPositions(): Set<Int> {
        return selectedPositions.toSet()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelections() {
        selectedPositions.clear()
        notifyDataSetChanged()
        selectionChangedListener?.invoke(selectedPositions)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun restoreSelections(savedPositions: Set<Int>) {
        selectedPositions.clear()
        selectedPositions.addAll(savedPositions)
        notifyDataSetChanged()
    }
}