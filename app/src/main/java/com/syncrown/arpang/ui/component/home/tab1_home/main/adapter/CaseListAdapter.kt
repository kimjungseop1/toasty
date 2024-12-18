package com.syncrown.arpang.ui.component.home.tab1_home.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.databinding.ItemCaseBinding
import com.syncrown.arpang.network.model.ResponseMainBannerDto

class CaseListAdapter(
    private val context: Context,
    private val sliderItems: ArrayList<ResponseMainBannerDto.Root>
) : RecyclerView.Adapter<CaseListAdapter.CaseHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int, clickLink: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseHolder {
        return CaseHolder(
            ItemCaseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CaseHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class CaseHolder(binding: ItemCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val eventBinding: ItemCaseBinding = binding
        fun onBind(context: Context, position: Int) {
            eventBinding.cardRootView.setOnClickListener {
                mListener?.onClick(position, sliderItems[position].click_link.toString())
            }

            Glide.with(context)
                .load(sliderItems[position].file_path)
                .into(eventBinding.caseImgView)

            eventBinding.caseTitleView.text = sliderItems[position].title
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}