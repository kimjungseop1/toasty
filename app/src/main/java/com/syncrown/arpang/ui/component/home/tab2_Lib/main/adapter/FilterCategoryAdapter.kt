package com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemCategoryParentBinding
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.Category

class FilterCategoryAdapter(
    private val context: Context,
    private val categories: List<Category>,
    private val onItemClick: (Int, FilterChildAdapter) -> Unit
) : RecyclerView.Adapter<FilterCategoryAdapter.CategoryViewHolder>() {

    private val childAdapters = categories.map { FilterChildAdapter(context, it.child) }

    private var selectedPosition = 0  // 처음 선택된 항목은 첫 번째 항목

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(private val binding: ItemCategoryParentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, isSelected: Boolean) {
            binding.categoryTitle.text = category.parentName

            if (isSelected) {
                binding.categoryTitle.setTextColor(
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

                binding.categoryTitle.typeface = Typeface.DEFAULT_BOLD
            } else {
                binding.categoryTitle.setTextColor(
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

                binding.categoryTitle.typeface = Typeface.DEFAULT
            }

            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onItemClick(bindingAdapterPosition, childAdapters[bindingAdapterPosition])
            }

        }
    }

    fun getChildAdapter(position: Int): FilterChildAdapter {
        return childAdapters[position]
    }
}