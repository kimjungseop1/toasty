package com.syncrown.arpang.ui.component.home.search.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ItemSearchInputBinding
import com.syncrown.arpang.network.model.ResponseSearchingMatchDto

class UserSearchingListAdapter(
    private val context: Context,
    private val items: ArrayList<ResponseSearchingMatchDto.Root>
) : RecyclerView.Adapter<UserSearchingListAdapter.UserSearchingHolder>() {

    private var searchText: String = ""

    interface OnItemClickListener {
        fun onClick(position: Int, nickNm: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchingHolder {
        return UserSearchingHolder(
            ItemSearchInputBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserSearchingHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class UserSearchingHolder(private val binding: ItemSearchInputBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(context: Context, position: Int) {
            binding.root.setOnClickListener {
                mListener?.onClick(position, items[position].nick_nm.toString())
            }

            val nickName = items[position].nick_nm ?: ""

            binding.searchText.text = nickName
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}