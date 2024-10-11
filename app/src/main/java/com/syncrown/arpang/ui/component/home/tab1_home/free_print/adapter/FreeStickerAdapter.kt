import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.RowTemplateBinding

class FreeStickerAdapter(
    private val context: Context
) : RecyclerView.Adapter<FreeStickerAdapter.ViewHolder>() {
    private var mIconListener: IconListener? = null

    fun setIconListener(iconListener: IconListener?) {
        mIconListener = iconListener
    }

    interface IconListener {
        fun onIconClick(bitmap: Bitmap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeStickerAdapter.ViewHolder {
        val binding = RowTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FreeStickerAdapter.ViewHolder, position: Int) {
        // Load image into the ImageView
        Glide.with(context)
            .asBitmap()
            .load(stickerPathList[position])
            .into(holder.binding.imgTemp)

    }

    override fun getItemCount(): Int {
        return stickerPathList.size
    }

    inner class ViewHolder(val binding: RowTemplateBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val adapterPosition = layoutPosition
                if (mIconListener != null) {
                    Glide.with(itemView.context)
                        .asBitmap()
                        .load(stickerPathList[adapterPosition])
                        .into(object : CustomTarget<Bitmap?>(256, 256) {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                                mIconListener?.onIconClick(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                }
            }
        }
    }

    private val stickerPathList = arrayOf(
        "https://cdn-icons-png.flaticon.com/256/4392/4392452.png",
        "https://cdn-icons-png.flaticon.com/256/4392/4392455.png",
        "https://cdn-icons-png.flaticon.com/256/4392/4392459.png",
        "https://cdn-icons-png.flaticon.com/256/4392/4392462.png",
        "https://cdn-icons-png.flaticon.com/256/4392/4392465.png",
        "https://cdn-icons-png.flaticon.com/256/4392/4392467.png",
        "https://cdn-icons-png.flaticon.com/256/4392/4392469.png",
        "https://cdn-icons-png.flaticon.com/256/4392/4392471.png",
        "https://cdn-icons-png.flaticon.com/256/4392/4392522.png",
    )
}
