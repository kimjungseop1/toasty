package com.syncrown.arpang.ui.component.home.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
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

    interface OnItemClickListener {
        fun onClick(position: Int, nickNm: String)
    }

    private var mListener: OnItemClickListener? = null

    private var query: String = ""

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateQuery(newQuery: String) {
        query = newQuery
        notifyDataSetChanged()
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
        holder.onBind(context, position, query)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class UserSearchingHolder(private val binding: ItemSearchInputBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(context: Context, position: Int, query: String) {
            binding.root.setOnClickListener {
                mListener?.onClick(position, items[position].nick_nm.toString())
            }

            val hashtagName = items[position].nick_nm
            val spannable = SpannableString(hashtagName)

            if (query.isNotEmpty()) {
                val startIndex = hashtagName!!.indexOf(query, ignoreCase = true)
                if (startIndex >= 0) {
                    val endIndex = startIndex + query.length
                    val selectedColor = ContextCompat.getColor(context, R.color.color_8e5d4b)
                    spannable.setSpan(
                        ForegroundColorSpan(selectedColor),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }

            // 기본 색상 설정
            binding.searchText.text = spannable
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}