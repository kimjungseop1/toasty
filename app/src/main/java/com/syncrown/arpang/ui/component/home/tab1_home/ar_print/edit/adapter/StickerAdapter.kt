package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.syncrown.arpang.databinding.RowStickerBinding
import com.syncrown.arpang.network.model.ResponseTemplateListDto

class StickerAdapter(
    private val context: Context,
    private val iconData: ArrayList<ResponseTemplateListDto.Root>
) : RecyclerView.Adapter<StickerAdapter.ViewHolder>() {
    private var mStickerListener: StickerListener? = null

    fun setStickerListener(stickerListener: StickerListener?) {
        mStickerListener = stickerListener
    }

    interface StickerListener {
        fun onStickerClick(bitmap: Bitmap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerAdapter.ViewHolder {
        val binding = RowStickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StickerAdapter.ViewHolder, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(iconData[position].file_path)
            .into(holder.binding.imgSticker)
    }

    override fun getItemCount(): Int {
        return iconData.size
    }

    inner class ViewHolder(val binding: RowStickerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val adapterPosition = layoutPosition
                if (mStickerListener != null) {
                    Glide.with(itemView.context)
                        .asBitmap()
                        .load(iconData[adapterPosition].file_path)
                        .into(object : CustomTarget<Bitmap?>(256, 256) {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                                mStickerListener?.onStickerClick(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                }
            }
        }
    }

}
