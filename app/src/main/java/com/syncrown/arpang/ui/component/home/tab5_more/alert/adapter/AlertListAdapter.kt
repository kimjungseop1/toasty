package com.syncrown.arpang.ui.component.home.tab5_more.alert.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.syncrown.arpang.databinding.ItemAlertListBinding
import com.syncrown.arpang.db.push_db.PushMessageEntity
import java.util.concurrent.TimeUnit

class AlertListAdapter(
    private val context: Context,
    private val items: List<PushMessageEntity>
) : RecyclerView.Adapter<AlertListAdapter.AlertHolder>() {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertHolder {
        return AlertHolder(
            ItemAlertListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlertHolder, position: Int) {
        holder.onBind(context, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AlertHolder(binding: ItemAlertListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val alertBinding: ItemAlertListBinding = binding
        fun onBind(context: Context, position: Int) {

            alertBinding.root.setOnClickListener {
                mListener?.onClick(position)
            }

            alertBinding.titleView.text = items[position].title

            alertBinding.contentView.text = items[position].content

            alertBinding.dateView.text = getTimeAgo(items[position].receiveTime)

            if (items[position].url.isEmpty()) {
                alertBinding.contentImage.visibility = View.GONE
            } else {
                alertBinding.contentImage.visibility = View.VISIBLE

                Glide.with(context)
                    .load(items[position].url)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(5)))
                    .into(alertBinding.contentImage)
            }
        }
    }

    fun getTimeAgo(timeInMillis: Long): String {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - timeInMillis

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> {
                val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
                "$seconds" + "초 전"
            }
            diff < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                "$minutes" + "분 전"
            }
            diff < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                "$hours" + "시간 전"
            }
            else -> {
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                "$days" + "일 전"
            }
        }
    }

    companion object {
        private var mListener: OnItemClickListener? = null
    }
}