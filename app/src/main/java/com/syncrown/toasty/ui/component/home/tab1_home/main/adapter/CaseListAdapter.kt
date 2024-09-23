package com.syncrown.toasty.ui.component.home.tab1_home.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.toasty.databinding.ItemCaseBinding
import com.syncrown.toasty.databinding.ItemMainEventBinding

class CaseListAdapter(
    private val context: Context,
    private val sliderItems: ArrayList<String>
) : RecyclerView.Adapter<CaseListAdapter.SlideBannerHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideBannerHolder {
        return SlideBannerHolder(
            ItemCaseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SlideBannerHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class SlideBannerHolder(binding: ItemCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val eventBinding: ItemCaseBinding = binding
        fun onBind(context: Context?, position: Int) {
            // 데이터 바인딩 로직
            eventBinding.cardRootView.setOnClickListener {
                mListener?.onClick(position)
            }
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}