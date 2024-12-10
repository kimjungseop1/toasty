package com.syncrown.arpang.ui.component.home.tab5_more.subscribe.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemSubscribeBinding
import com.syncrown.arpang.network.model.ResponseSubscribeListDto
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.SubscribeType

class SubscribeListAdapter(
    private val context: Context,
    private val items: ArrayList<ResponseSubscribeListDto.Root>,
    private val type: SubscribeType,
    private var mDelListener: OnItemDeleteListener,
    private var mListener: OnItemClickListener,
    private var mSubscribe: OnSubscribeListener
) : RecyclerView.Adapter<SubscribeListAdapter.SubscribeHolder>() {

    interface OnItemDeleteListener {
        fun onDelete(position: Int, view: View)
    }

    interface OnItemClickListener {
        fun onClick(position: Int, subUserId: ResponseSubscribeListDto.Root)
    }

    interface OnSubscribeListener {
        fun onSubscribe(position: Int, subUserId: ResponseSubscribeListDto.Root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribeHolder {
        return SubscribeHolder(
            ItemSubscribeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubscribeHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SubscribeHolder(private val binding: ItemSubscribeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(context: Context, position: Int) {
            if (type == SubscribeType.MY) {
                binding.moreView.visibility = View.GONE
                binding.titleView.text = items[position].sub_nick_nm
                binding.contentView.text =  "${items[position].sub_user_share_cnt}건의 공개된 게시글"
            } else {
                binding.moreView.visibility = View.VISIBLE
                binding.titleView.text = items[position].sub_nick_nm
                binding.contentView.text =  "${items[position].sub_user_share_cnt}건의 공개된 게시글"
                if (items[position].is_sub_me == 1) {
                    Glide.with(context)
                        .load(R.drawable.icon_like_sel)
                        .into(binding.likeView)
                } else {
                    Glide.with(context)
                        .load(R.drawable.icon_like_unsel)
                        .into(binding.likeView)
                }
            }

            binding.subscribeView.setOnClickListener {
                mSubscribe.onSubscribe(position, items[position])
            }

            binding.moreView.setOnClickListener {
                mDelListener.onDelete(position, binding.moreView)
            }

            binding.root.setOnClickListener {
                mListener.onClick(position, items[position])
            }
        }
    }

}